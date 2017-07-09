package com.figure.msclient.hystrix;

import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;

/**
 * Created by chuanbo.wei on 2017/3/2.
 */
public class MSHystrixPropertiesStrategy extends HystrixPropertiesStrategy {

    @Override
    public String getCommandPropertiesCacheKey(HystrixCommandKey commandKey, HystrixCommandProperties.Setter builder) {
        Integer timeout = builder.getExecutionTimeoutInMilliseconds();
        if(null!=timeout){
            return commandKey.name() + timeout;
        }
        return commandKey.name();
    }
}
