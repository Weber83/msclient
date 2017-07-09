package com.figure.msclient.hystrix;

import com.figure.msclient.config.MSConfig;
import com.figure.msclient.mservice.InvokeContext;
import com.figure.msclient.mservice.ReferenceService;
import com.netflix.hystrix.strategy.HystrixPlugins;

/**
 * Created by chuanbo.wei on 2017/2/21.
 */
public class MSCommandFactory implements CommandFactory {

    private HystrixProperties hystrixProperties;

    public MSCommandFactory(MSConfig msConfig){
        hystrixProperties = new HystrixProperties();
        if(null!=msConfig.getExecutionTimeoutInMilliseconds() && msConfig.getExecutionTimeoutInMilliseconds()>0){
            hystrixProperties.setExecutionTimeoutInMilliseconds(msConfig.getExecutionTimeoutInMilliseconds());
        }
        if(null!=msConfig.getCircuitBreakerErrorThresholdPercentage() && msConfig.getCircuitBreakerErrorThresholdPercentage()>0){
            hystrixProperties.setCircuitBreakerErrorThresholdPercentage(msConfig.getCircuitBreakerErrorThresholdPercentage());
        }
        if(null!=msConfig.getCircuitBreakerSleepWindowInMilliseconds()&& msConfig.getCircuitBreakerSleepWindowInMilliseconds()>0){
            hystrixProperties.setCircuitBreakerSleepWindowInMilliseconds(msConfig.getCircuitBreakerSleepWindowInMilliseconds());
        }
        if(null!=msConfig.getExecutionIsolationSemaphoreMaxConcurrentRequests() && msConfig.getExecutionIsolationSemaphoreMaxConcurrentRequests()>0){
            hystrixProperties.getSemaphore().setMaxSemaphores(msConfig.getExecutionIsolationSemaphoreMaxConcurrentRequests());
        }
//        HystrixPlugins.getInstance().registerPropertiesStrategy(new MSHystrixPropertiesStrategy());
    }

    @Override
    public MSCommand create(ReferenceService service, InvokeContext context) {
//        String serviceId = null;
//        if(ServiceRpcTypeConstant.SPRINGCLOUD.getType()==context.getApi().getProtocolType()){
//            serviceId = context.getApi().getInterfaceName();
//        }else {
//            serviceId = context.getApi().getCategory();
//        }
        return new MSCommand(context.getMethodName(), service, context, hystrixProperties);
    }
}
