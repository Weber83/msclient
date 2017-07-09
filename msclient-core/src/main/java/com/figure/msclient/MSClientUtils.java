package com.figure.msclient;

import com.figure.msclient.config.ConfigLoader;
import com.figure.msclient.constant.MSClientModel;
import com.figure.msclient.metadata.ParaMeta;
import com.figure.msclient.util.Assert;
import com.figure.msclient.util.StringUtils;

import java.util.List;

/**
 * Created by chuanbo.wei on 2017/3/20.
 */
public class MSClientUtils {

    private static MSClientUtils instance = new MSClientUtils();

    private MSClientUtils(){}

    private static MSClient msClient = null;

    public static MSClientUtils getInstance() {
        return instance;
    }

    public void init(String appName, String loader, String model){
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
        }
//        else {
//            l = new XConfigConfigLoader();
//        }
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
    }

    public static Object invoke(String id, String methodName, String requestType, List<ParaMeta> paraList) throws MSClientException {
        return msClient.invoke(id, methodName, requestType, paraList);
    }
}
