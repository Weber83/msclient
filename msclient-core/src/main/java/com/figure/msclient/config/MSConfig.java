package com.figure.msclient.config;

import com.figure.msclient.config.dubbo.DubboProperties;
import com.figure.msclient.config.springcloud.EurekaClientProperties;

/**
 * Created by chuanbo.wei on 2017/3/28.
 */
public class MSConfig {

    private EurekaClientProperties eurekaClientProperties;

    private DubboProperties dubboProperties;

    private String msDebug = "false";

    //熔断
    private Integer executionTimeoutInMilliseconds;

    private Integer circuitBreakerErrorThresholdPercentage;

    private Integer circuitBreakerSleepWindowInMilliseconds;

    private Integer executionIsolationSemaphoreMaxConcurrentRequests;

    public EurekaClientProperties getEurekaClientProperties() {
        return eurekaClientProperties;
    }

    public void setEurekaClientProperties(EurekaClientProperties eurekaClientProperties) {
        this.eurekaClientProperties = eurekaClientProperties;
    }

    public DubboProperties getDubboProperties() {
        return dubboProperties;
    }

    public void setDubboProperties(DubboProperties dubboProperties) {
        this.dubboProperties = dubboProperties;
    }

    public String getMsDebug() {
        return msDebug;
    }

    public void setDsDebug(String msDebug) {
        this.msDebug = msDebug;
    }

    public Integer getExecutionTimeoutInMilliseconds() {
        return executionTimeoutInMilliseconds;
    }

    public void setExecutionTimeoutInMilliseconds(Integer executionTimeoutInMilliseconds) {
        this.executionTimeoutInMilliseconds = executionTimeoutInMilliseconds;
    }

    public Integer getCircuitBreakerErrorThresholdPercentage() {
        return circuitBreakerErrorThresholdPercentage;
    }

    public void setCircuitBreakerErrorThresholdPercentage(Integer circuitBreakerErrorThresholdPercentage) {
        this.circuitBreakerErrorThresholdPercentage = circuitBreakerErrorThresholdPercentage;
    }

    public Integer getCircuitBreakerSleepWindowInMilliseconds() {
        return circuitBreakerSleepWindowInMilliseconds;
    }

    public void setCircuitBreakerSleepWindowInMilliseconds(Integer circuitBreakerSleepWindowInMilliseconds) {
        this.circuitBreakerSleepWindowInMilliseconds = circuitBreakerSleepWindowInMilliseconds;
    }

    public Integer getExecutionIsolationSemaphoreMaxConcurrentRequests() {
        return executionIsolationSemaphoreMaxConcurrentRequests;
    }

    public void setExecutionIsolationSemaphoreMaxConcurrentRequests(Integer executionIsolationSemaphoreMaxConcurrentRequests) {
        this.executionIsolationSemaphoreMaxConcurrentRequests = executionIsolationSemaphoreMaxConcurrentRequests;
    }
}
