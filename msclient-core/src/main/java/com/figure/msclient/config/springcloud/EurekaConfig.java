package com.figure.msclient.config.springcloud;

import com.figure.msclient.config.EnvConfig;
import com.figure.msclient.sc.config.MSEurekaClientConfig;
import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;

import javax.inject.Provider;

/**
 * Created by chuanbo.wei on 2017/3/12.
 */
public class EurekaConfig implements EnvConfig {

    private ApplicationInfoManager applicationInfoManager;

    private MSEurekaClientConfig eurekaClientConfig;

    private Provider<EurekaClient> provider;

    private DiscoveryClient discoveryClient;

    public EurekaConfig(ApplicationInfoManager applicationInfoManager, MSEurekaClientConfig eurekaClientConfig){
        this.applicationInfoManager = applicationInfoManager;
        this.eurekaClientConfig = eurekaClientConfig;
        discoveryClient = new DiscoveryClient(this.applicationInfoManager, this.eurekaClientConfig);
        this.provider = new Provider<EurekaClient>() {
            @Override
            public EurekaClient get() {
                return discoveryClient;
            }
        };
    }

    public Provider<EurekaClient> getProvider(){
        return provider;
    }
}
