package com.figure.msclient.config.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.figure.msclient.config.EnvConfig;

/**
 * Created by chuanbo.wei on 2017/3/12.
 */
public class DubboConfig extends RegistryConfig implements EnvConfig {

    private ApplicationConfig application;

    public RegistryConfig registry;

    public ApplicationConfig getApplication() {
        return application;
    }

    public void setApplication(ApplicationConfig application) {
        this.application = application;
    }

    public RegistryConfig getRegistry() {
        return registry;
    }

    public void setRegistry(RegistryConfig registry) {
        this.registry = registry;
    }
}
