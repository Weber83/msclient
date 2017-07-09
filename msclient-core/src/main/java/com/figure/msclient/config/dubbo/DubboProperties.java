package com.figure.msclient.config.dubbo;

/**
 * Created by chuanbo.wei on 2017/3/13.
 */
public class DubboProperties {

    private String appName;

    private String registryProtocol = "zookeeper";

    private String registryAddress;

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String getRegistryProtocol() {
        return registryProtocol;
    }

    public void setRegistryProtocol(String registryProtocol) {
        this.registryProtocol = registryProtocol;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
