package com.figure.msclient.config.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.figure.msclient.Env;

/**
 * Created by chuanbo.wei on 2017/3/10.
 */
public class DubboEnv implements Env<DubboConfig>{

    private DubboProperties properties;

    /**
     * 默认的当前环境
     */
    private DubboConfig defaultConfig;

    /**
     * 灰度环境，如果开启灰度的话，会初始化该环境配置
     */
    private DubboConfig grayConfig;

    private static Object lock = new Object();

    public DubboEnv(DubboProperties properties){
        this.properties = properties;
    }

    @Override
    public void init() {
        //初始化dubbo应用配置
        if(null==defaultConfig){
            synchronized (lock){
                if(null==defaultConfig){
                    defaultConfig = new DubboConfig();
                    ApplicationConfig application = new ApplicationConfig();
                    application.setName(properties.getAppName());

                    //初始化dubbo注册中心配置
                    RegistryConfig registry = new RegistryConfig();
                    registry.setProtocol(properties.getRegistryProtocol());
                    registry.setAddress(properties.getRegistryAddress());
                    defaultConfig.setApplication(application);
                    defaultConfig.setRegistry(registry);
                }
            }
        }
    }

    @Override
    public DubboConfig getDefault() {
        return defaultConfig;
    }

    @Override
    public DubboConfig getGray() {
        return grayConfig;
    }
}
