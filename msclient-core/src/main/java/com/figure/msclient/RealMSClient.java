package com.figure.msclient;

import com.figure.msclient.config.ConfigLoader;
import com.figure.msclient.config.MSConfig;
import com.figure.msclient.config.dubbo.DubboConfig;
import com.figure.msclient.config.dubbo.DubboEnv;
import com.figure.msclient.config.ms.MsReferenceManage;
import com.figure.msclient.config.springcloud.EurekaConfig;
import com.figure.msclient.config.springcloud.EurekaEnv;
import com.figure.msclient.constant.MSClientModel;
import com.figure.msclient.constant.MSErrorType;
import com.figure.msclient.constant.ServiceRpcTypeConstant;
import com.figure.msclient.hystrix.Command;
import com.figure.msclient.hystrix.CommandFactory;
import com.figure.msclient.hystrix.MSCommandFactory;
import com.figure.msclient.metadata.ParaMeta;
import com.figure.msclient.metadata.ReferenceBean;
import com.figure.msclient.mservice.*;
import com.figure.msclient.mservice.dubbo.DubboReferenceService;
import com.figure.msclient.mservice.springcloud.RestReferenceService;
import com.figure.msclient.processor.MSGlobalInfoStore;
import com.figure.msclient.processor.ServerCommandCenter;
import com.figure.msclient.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by chuanbo.wei on 2017/3/11.
 */
public class RealMSClient extends MSClient {

    private static final Logger logger = LoggerFactory.getLogger(RealMSClient.class);

    private ConfigLoader configLoader;

    private String appName;

    private MSClientModel msClientModel;

    private Env<DubboConfig> dubboEnv;

    private Env<EurekaConfig> eurekaEnv;

    private MSConfig msConfig;

    private CommandFactory<?> commandFactory;

    //[msKey : referenceService]
    private static final ConcurrentMap<String, ReferenceServiceWarp> msCache = new ConcurrentHashMap<String, ReferenceServiceWarp>(5);

    //灰度RPC service[msKey : referenceService]
    private static ConcurrentMap<String, ReferenceServiceWarp> grayMsCache = new ConcurrentHashMap<String, ReferenceServiceWarp>();

    private static volatile Set<String> ifKeyCache = new HashSet<String>();

    RealMSClient(String appName, ConfigLoader configLoader, MSClientModel msClientModel){
        this.appName = appName;
        this.configLoader = configLoader;
        this.msClientModel = msClientModel;
        init();
    }

    private void init(){
        configLoader.init(appName, msClientModel);
        msConfig = configLoader.load();

        MSGlobalInfoStore.getInstance().setDsConfig(msConfig);

        if(MSClientModel.FULL.equals(msClientModel)){
            dubboEnv = new DubboEnv(msConfig.getDubboProperties());
            dubboEnv.init();

            eurekaEnv = new EurekaEnv(msConfig.getEurekaClientProperties());
            eurekaEnv.init();
        } else if(MSClientModel.DUBBO.equals(msClientModel)){
            dubboEnv = new DubboEnv(msConfig.getDubboProperties());
            dubboEnv.init();
        } else if(MSClientModel.SPRINGCLOUD.equals(msClientModel)){
            eurekaEnv = new EurekaEnv(msConfig.getEurekaClientProperties());
            eurekaEnv.init();
        }

        commandFactory = new MSCommandFactory(msConfig);

        MsReferenceManage.getInstance().init();
        try {
            this.registry(MsReferenceManage.getInstance().getBeans());
        } catch (MSInitException e) {
            e.printStackTrace();
        }

        if ("true".equalsIgnoreCase(msConfig.getMsDebug())){
            ServerCommandCenter.init();
        }
        logger.info("MSClient init ok.");
    }

    private void registry(List<ReferenceBean> beans)throws MSInitException{
        if(null==beans || beans.size()<=0)
            return;
        for (ReferenceBean bean : beans){
            this.registry(bean.getId(), bean.getServiceId(), bean.getVersion(), bean.getGroup(), bean.getProtocolType());
            logger.info("MSClient MS registry of " + bean.getId());
        }
        logger.info("MSClient MS registry ok.");
    }

    @Override
    public void registry(String id, String serviceId, String serviceVersion, String serviceGroup, int protocolType) throws MSInitException{
        ReferenceServiceWarp referenceService = null;
        ReferenceService rc = null;
//        String apiKey = takeApiKey(apiName, version);
        referenceService = msCache.get(id);
        if(null!=referenceService){//防止重复计数
            logger.warn("ms-config.xml配置错误，id重复，" + id);
            throw new IllegalArgumentException("ms-config.xml配置错误，id重复，" + id);
        }
        String serviceKey = takeServiceKey(serviceId, serviceVersion, serviceGroup, protocolType);
        if(ifKeyCache.contains(serviceKey)){//防止重复初始化服务
            logger.warn("服务配置错误，服务重复，" + serviceKey);
            throw new IllegalArgumentException("服务配置错误，服务重复，" + serviceKey);
        }

        try {
            if(null==referenceService){
                if(ServiceRpcTypeConstant.SPRINGCLOUD.getType()==protocolType){
                    rc = new RestReferenceService(serviceId, eurekaEnv.getDefault());
                }else if(ServiceRpcTypeConstant.DUBBO.getType()==protocolType){
                    rc = new DubboReferenceService(serviceId, serviceVersion, serviceGroup, dubboEnv.getDefault());
                }
                if(null!=rc){
                    rc.init();
                    referenceService = new ReferenceServiceWarp(rc);
                    msCache.put(id, referenceService);
//                    addApiKeyCache(apiKey);
                    ifKeyCache.add(serviceKey);
                }else {
                    logger.error("MS 初始化失败，" + id);
                    throw new MSInitException("MS 初始化失败，" + id);
                }
            }
//            else {
//                referenceService.addReferenceCount();//增加引用计数
//            }
        }catch (Exception e){
            logger.error("MS 初始化失败，" + id);
            throw new MSInitException(e);
        }
    }

    @Override
    public Map<String, Object> invoke(String id, String methodName, String requestType, List<ParaMeta> paraList) throws MSClientException {
//        String serviceKey = takeServiceKey(serviceId, serviceVersion, serviceGroup, protocolType);
        ReferenceServiceWarp referenceService = msCache.get(id);
        if(null==referenceService){
            throw new MSClientException(MSErrorType.MS_NOT_INIT_EXP);
        }
        InvokeContext context = new InvokeContext();
        context.setMethodName(methodName);
        context.setRequestType(requestType);
        context.setParaList(paraList);
        Command command = commandFactory.create(referenceService.getService(), context);
        Map<String, Object> result = null;
        try {
            result = command.execute();
        } catch (Exception e){
            if(null!=context.getExp()){
                throw MSExceptionHelpr.generateDsException(context.getExp());
            }
            throw MSExceptionHelpr.generateDsException(e);
        }finally {
            context = null;
        }
        return result;
    }

    @Override
    public void destroy(String id) {
//        String serviceKey = takeServiceKey(serviceId, serviceVersion, serviceGroup, protocolType);
        ReferenceServiceWarp referenceService = msCache.get(id);
        if(null!=referenceService){//old 计数减一
//            if(referenceService.decrement()<=0){
//
//            }
            try {
                referenceService.getService().destroy();
                msCache.remove(id);
                referenceService = null;
                ifKeyCache.remove(id);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("销毁MS服务失败," + id);
            }
        }
    }

//    private void addApiKeyCache(String apiKey){
//        if(!apiKeyCache.contains(apiKey)){
//            synchronized (lock){
//                apiKeyCache.add(apiKey);
//            }
//        }
//    }

    private String takeApiKey(String apiName, String version){
        String apiKey = apiName;
        if(StringUtils.isNotEmpty(version))
            apiKey += ":" + version;
        return apiKey;
    }

    private String takeServiceKey(String serviceId, String serviceVersion, String serviceGroup, int protocolType){
        String interfaceKey = serviceId;
        if(StringUtils.isNotEmpty(serviceVersion))
            interfaceKey += ":" + serviceVersion;
        if(ServiceRpcTypeConstant.DUBBO.getType()==protocolType && StringUtils.isNotEmpty(serviceGroup))
            interfaceKey += ":" + serviceGroup;
        return interfaceKey;
    }

    public static ConcurrentMap<String, ReferenceServiceWarp> getMsCache() {
        return msCache;
    }

    public static Set<String> getIfKeyCache() {
        return ifKeyCache;
    }
}
