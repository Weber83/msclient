package com.figure.msclient.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by chuanbo.wei on 2017/8/01
 */
@ConfigurationProperties(prefix = MSClientProperties.MSCLIIENT_PREFIX)
public class MSClientProperties {
    public static final String MSCLIIENT_PREFIX = "msclient";

    private String appName;

    private String loader;

    private String model;

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
}
