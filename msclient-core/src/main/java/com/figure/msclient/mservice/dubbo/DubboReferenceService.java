package com.figure.msclient.mservice.dubbo;

import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.service.GenericException;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.figure.msclient.MSClientException;
import com.figure.msclient.config.dubbo.DubboConfig;
import com.figure.msclient.constant.ParamType;
import com.figure.msclient.constant.ServiceRpcTypeConstant;
import com.figure.msclient.metadata.ParaMeta;
import com.figure.msclient.mservice.MSExceptionHelpr;
import com.figure.msclient.mservice.InvokeContext;
import com.figure.msclient.mservice.ProxyRequestHelper;
import com.figure.msclient.mservice.ReferenceService;
import com.figure.msclient.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by chuanbo.wei on 2017/3/10.
 */
public class DubboReferenceService extends ReferenceConfig<GenericService> implements ReferenceService{
    private static final Logger logger = LoggerFactory.getLogger(DubboReferenceService.class);

    public static final int INVOKE_TIMEOUT = 10000;

    public DubboReferenceService(String interfaceName, String serviceVersion, String serviceGroup, DubboConfig config){
        this(interfaceName, serviceVersion, serviceGroup, config, false, true);
    }

    public DubboReferenceService(String interfaceName, String serviceVersion, String serviceGroup, DubboConfig config, boolean check, boolean async){
        super();
        this.setApplication(config.getApplication());
        this.setRegistry(config.getRegistry());
        //弱类型接口名
        this.setInterface(interfaceName);
        //声明为泛化接口
        this.setGeneric(true);
        this.setCheck(check);
        this.setAsync(async);
//        this.setVersion(api.getVersion());
        if(StringUtils.isNotEmpty(serviceVersion)){
            this.setVersion(serviceVersion);
        }
        if(StringUtils.isNotEmpty(serviceGroup)){
            this.setGroup(serviceGroup);
        }
    }

    @Override
    public void init() {
        //实时初始化
        try {
            this.get();
        } catch (Exception e) {
            logger.error("初始化API服务失败，API配置为：" + this.getInterface() + ":" + this.getVersion());
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> invoke(InvokeContext context) throws Exception {
        Future<Object> future = null;
        String[] types = null;
        Object[] values = null;
        List<ParaMeta> paraList = context.getParaList();
        if(null!=paraList && paraList.size()>0){
            types = new String[paraList.size()];
            values = new Object[paraList.size()];
            ParaMeta paraMeta = null;
            for (int i=0; i<paraList.size(); i++) {
                paraMeta = paraList.get(i);
                types[i] = ParamType.getClazzName(paraMeta.getType().getTypeName());
                values[i] = paraMeta.getValue();
            }
        }
        Map<String, Object> map = null;
        try {
            Object result = get().$invoke(context.getMethodName(), types, values);
            future = RpcContext.getContext().getFuture();
            if(result == null && future != null){
                result = future.get(INVOKE_TIMEOUT, TimeUnit.MILLISECONDS);
            }
            if(null!=result){
                ProxyRequestHelper.markClassTag(result);
                map = (Map)result;
            }
        } catch (Exception e) {
//            throw MSExceptionHelpr.generateDsException(e);
            context.setExp(e);
            if(!ProxyRequestHelper.checkBizExp(e)){
                throw e;
            }
        } finally {
            try {
                if(future != null && !future.isCancelled()){
                    future.cancel(true);
                }
            } catch (Exception e) {}
        }
        return map;
    }

    @Override
    public void destroy() {
        this.destroy();
    }

    @Override
    public int rpcType() {
        return ServiceRpcTypeConstant.DUBBO.getType();
    }
}
