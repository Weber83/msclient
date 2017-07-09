package com.figure.msclient.config.springcloud;

import com.figure.msclient.Env;
import com.figure.msclient.sc.config.MSEurekaClientConfig;
import com.figure.msclient.sc.config.MSEurekaInstanceConfig;
import com.figure.msclient.sc.util.InetUtils;
import com.figure.msclient.sc.util.InetUtilsProperties;
import com.netflix.appinfo.ApplicationInfoManager;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chuanbo.wei on 2017/3/10.
 */
public class EurekaEnv implements Env<EurekaConfig> {

    private EurekaClientProperties properties;

    /**
     * 默认的当前环境
     */
    private EurekaConfig defaultConfig;

    /**
     * 灰度环境，如果开启灰度的话，会初始化该环境配置
     */
    private EurekaConfig grayConfig;

    public EurekaEnv(EurekaClientProperties properties){
        this.properties = properties;
    }

    @Override
    public void init() {
        if(null==defaultConfig) {
            MSEurekaClientConfig bean = new MSEurekaClientConfig();
            Map<String, String> map = new HashMap<String, String>();
            map.put(MSEurekaClientConfig.DEFAULT_ZONE, properties.getDefaultZone());//"http://10.25.239.62:1062/eureka/,http://10.25.239.63:1063/eureka/,http://10.25.239.64:1064/eureka/"
            bean.setServiceUrl(map);
            bean.setRegisterWithEureka(false);

            MSEurekaInstanceConfig instanceConfigBean = new MSEurekaInstanceConfig(new InetUtils(new InetUtilsProperties()));
            instanceConfigBean.setPreferIpAddress(properties.isPreferIpAddress());
            instanceConfigBean.setAppGroupName("MSClinet");
            instanceConfigBean.setAppname("MSClinet-" + instanceConfigBean.getIpAddress());
            ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(instanceConfigBean);
            defaultConfig = new EurekaConfig(applicationInfoManager, bean);
        }
    }

    @Override
    public EurekaConfig getDefault() {
        return defaultConfig;
    }

    @Override
    public EurekaConfig getGray() {
        return grayConfig;
    }
}
