package com.figure.msclient.config.loader;

import com.figure.msclient.config.MSConfig;
import com.figure.msclient.config.dubbo.DubboProperties;
import com.figure.msclient.config.springcloud.EurekaClientProperties;
import com.figure.msclient.constant.Constants;
import com.figure.msclient.constant.MSClientModel;
import com.figure.msclient.util.Assert;
import com.figure.msclient.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * load config form classpath for properties
 * Created by chuanbo.wei on 2017/3/10.
 */
public class PropertiesConfigLoader extends AbstractConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesConfigLoader.class);
    //properites name
    public final static String MS_PRO_NAME = "/ms.properties";

    private Properties properties = null;

    @Override
    public void init(String appName, MSClientModel msClientModel) {
        InputStream input = this.getClass().getResourceAsStream(MS_PRO_NAME);
        properties = new Properties();
        try {
            properties.load(input);
        } catch (IOException e) {
            logger.error("初始化加载properties配置失败!" + MS_PRO_NAME);
            e.printStackTrace();
        }
        this.msClientModel = msClientModel;
        this.appName = appName;
        logger.info("MSClient PropertiesConfigLoader init ok.");
    }

    public void loadCommonProperties(MSConfig msConfig){
        String msDebug = properties.getProperty(Constants.MS_DEBUG);//
        if(StringUtils.isNotEmpty(msDebug) && ("false".equalsIgnoreCase(msDebug) || "true".equalsIgnoreCase(msDebug))){
            msConfig.setDsDebug(msDebug);
        }
        String circuitBreakerErrorThresholdPercentage = properties.getProperty(Constants.MS_CIRCUITBREAKERERRORTHRESHOLDPERCENTAGE);
        if(StringUtils.isNotEmpty(circuitBreakerErrorThresholdPercentage)){
            Integer iCircuitBreakerErrorThresholdPercentage = new Integer(circuitBreakerErrorThresholdPercentage);
            if(iCircuitBreakerErrorThresholdPercentage<1 || iCircuitBreakerErrorThresholdPercentage>99){
                throw new IllegalArgumentException(Constants.MS_CIRCUITBREAKERERRORTHRESHOLDPERCENTAGE + " 取值范围为[1,99]");
            }
            msConfig.setCircuitBreakerErrorThresholdPercentage(iCircuitBreakerErrorThresholdPercentage);
        }
        String circuitBreakerSleepWindowInMilliseconds = properties.getProperty(Constants.MS_CIRCUITBREAKERSLEEPWINDOWINMILLISECONDS);
        if(StringUtils.isNotEmpty(circuitBreakerSleepWindowInMilliseconds)){
            Integer iCircuitBreakerSleepWindowInMilliseconds = new Integer(circuitBreakerSleepWindowInMilliseconds);
            msConfig.setCircuitBreakerSleepWindowInMilliseconds(iCircuitBreakerSleepWindowInMilliseconds);
        }
        String maxSemaphores = properties.getProperty(Constants.MS_EXECUTIONISOLATIONSEMAPHOREMAXCONCURRENTREQUESTS);
        if(StringUtils.isNotEmpty(maxSemaphores)){
            Integer iMaxSemaphores = new Integer(maxSemaphores);
            if(iMaxSemaphores<1){
                throw new IllegalArgumentException(Constants.MS_EXECUTIONISOLATIONSEMAPHOREMAXCONCURRENTREQUESTS + " 取值不正确");
            }
            msConfig.setExecutionIsolationSemaphoreMaxConcurrentRequests(iMaxSemaphores);
        }
        String executionTimeoutInMilliseconds = properties.getProperty(Constants.MS_EXECUTIONTIMEOUTINMILLISECONDS);
        if(StringUtils.isNotEmpty(executionTimeoutInMilliseconds)){
            Integer iExecutionTimeoutInMilliseconds = new Integer(executionTimeoutInMilliseconds);
            msConfig.setExecutionTimeoutInMilliseconds(iExecutionTimeoutInMilliseconds);
        }
    }

    public void loadEurekaProperties(MSConfig msConfig){
        EurekaClientProperties eurekaClientProperties = new EurekaClientProperties();

        String defaultZone = properties.getProperty(Constants.EUREKA_DEFAULT_ZONE1);//
        Assert.notNull(defaultZone, "defaultZone must not be null.");
        eurekaClientProperties.setDefaultZone(defaultZone);
        msConfig.setEurekaClientProperties(eurekaClientProperties);
//        String preferIpAddress = xConfig.getValue(Constants.EUREKA_DEFAULT_ZONE);//
    }

    public void loadDubboProperties(MSConfig msConfig){
        DubboProperties dubboProperties = new DubboProperties();
        dubboProperties.setAppName(appName);

        String registryAddrss = properties.getProperty(Constants.DUBBO_REGIDTRY_ADDRESS);//dubbo 注册中心地址
        Assert.notNull(registryAddrss,"dubbo registry addrss must not be null.");
        dubboProperties.setRegistryAddress(registryAddrss);

        String registryProtocol = properties.getProperty(Constants.DUBBO_REGIDTRY_PROTOCOL);
        if(StringUtils.isNotEmpty(registryProtocol)){
            dubboProperties.setRegistryProtocol(registryProtocol);
        }
        msConfig.setDubboProperties(dubboProperties);
    }
}
