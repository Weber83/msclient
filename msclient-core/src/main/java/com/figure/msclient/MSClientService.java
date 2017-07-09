package com.figure.msclient;

import com.figure.msclient.config.ConfigLoader;
import com.figure.msclient.config.loader.PropertiesConfigLoader;
import com.figure.msclient.constant.MSClientModel;
import com.figure.msclient.metadata.ParaMeta;
import com.figure.msclient.metadata.ReferenceBean;
import com.figure.msclient.mservice.MSInitException;
import com.figure.msclient.util.Assert;
import com.figure.msclient.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by chuanbo.wei on 2017/3/20.
 */
public class MSClientService {

    public MSClientService(){

    }

    public MSClientService(String appName, String loader, String model){
        this(appName, loader, model, null);
    }

    public MSClientService(String appName, String loader, String model, MsReferenceProvide provide){
        this.appName = appName;
        this.loader = loader;
        this.model = model;
        this.provide = provide;
    }

    private String appName;

    private String loader;

    private String model;

    private MsReferenceProvide provide;

    private static MSClient msClient = null;

    public void init(){
        ConfigLoader l = null;
        if(StringUtils.isNotEmpty(loader)){
            try {
                Class c = Class.forName(loader);
                if(null!=c){
                    l = (ConfigLoader)c.newInstance();
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            Assert.notNull(l, "配置加载器不能为null");
        }else {
            l = new PropertiesConfigLoader();
        }
        MSClientModel msClientModel = null;
        if(StringUtils.isNotEmpty(model)){
            if("dubbo".equalsIgnoreCase(model)){
                msClientModel = MSClientModel.DUBBO;
            } else if("springcloud".equalsIgnoreCase(model)){
                msClientModel = MSClientModel.SPRINGCLOUD;
            } else if("full".equalsIgnoreCase(model)){
                msClientModel = MSClientModel.FULL;
            } else {
                throw new IllegalArgumentException("model配置项错误，正确的取值为dubbo、springcloud或full");
            }
        } else {
            msClientModel = MSClientModel.FULL;
        }
        msClient = MSClient.builder().appName(appName).configLoader(l).model(msClientModel).build();
        if(null!=provide){
            if (null!=provide.getReferences() && provide.getReferences().size()>0){
                for (ReferenceBean bean :provide.getReferences()){
                    try {
                        msClient.registry(bean.getId(), bean.getServiceId(), bean.getVersion(), bean.getGroup(), bean.getProtocolType());
                    } catch (MSInitException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public Map<String, Object> invoke(String id, String methodName, String requestType, List<ParaMeta> paraList) throws MSClientException {
        return msClient.invoke(id, methodName, requestType, paraList);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLoader() {
        return loader;
    }

    public void setLoader(String loader) {
        this.loader = loader;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public MsReferenceProvide getProvide() {
        return provide;
    }

    public void setProvide(MsReferenceProvide provide) {
        this.provide = provide;
    }
}
