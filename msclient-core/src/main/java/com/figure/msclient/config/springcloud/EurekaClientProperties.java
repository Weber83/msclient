package com.figure.msclient.config.springcloud;

/**
 * Created by chuanbo.wei on 2017/3/13.
 */
public class EurekaClientProperties {

    private String defaultZone;

    private boolean preferIpAddress =true;


    public String getDefaultZone() {
        return defaultZone;
    }

    public void setDefaultZone(String defaultZone) {
        this.defaultZone = defaultZone;
    }

    public boolean isPreferIpAddress() {
        return preferIpAddress;
    }

    public void setPreferIpAddress(boolean preferIpAddress) {
        this.preferIpAddress = preferIpAddress;
    }
}
