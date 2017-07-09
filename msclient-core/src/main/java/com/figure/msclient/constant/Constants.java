package com.figure.msclient.constant;

/**
 * Created by chuanbo.wei on 2017/3/10.
 */
public class Constants {

    //dubbo
    //dubbo 注册中心协议
    public final static String DUBBO_REGIDTRY_PROTOCOL = "base-zk.registry";

    //dubbo 注册中心地址
    public final static String DUBBO_REGIDTRY_ADDRESS = "base-zk.inner.connStr";

    //springcloud

    public final static String EUREKA_DEFAULT_ZONE1 = "base-eureka.node1";

    public final static String EUREKA_DEFAULT_ZONE2 = "base-eureka.node2";

    public final static String EUREKA_DEFAULT_ZONE3 = "base-eureka.node13";

    public final static String MS_DEBUG = "ms.debug";//是否打开内省信息标志

    //熔断
    public final static String MS_EXECUTIONTIMEOUTINMILLISECONDS = "ms.executionTimeoutInMilliseconms";//调用依赖超时时间毫秒数，如超时则抛出HystrixTimeoutException；executionTimeoutEnabled=true时有效

    public final static String MS_CIRCUITBREAKERERRORTHRESHOLDPERCENTAGE = "ms.circuitBreakerErrorThresholdPercentage";//失败请求量占总请求量的百分比大于该值时，开启熔断；失败请求包括异常、超时、超线程数拒绝、超信号量拒绝

    public final static String MS_CIRCUITBREAKERSLEEPWINDOWINMILLISECONDS = "ms.circuitBreakerSleepWindowInMilliseconds";//熔断后，允许再次尝试请求的间隔毫秒数，默认3秒

    //采用信号量隔离时，最大并发请求量，超过该值时则拒绝请求；msclient默认为80。 Number of permits for execution semaphore
    public final static String MS_EXECUTIONISOLATIONSEMAPHOREMAXCONCURRENTREQUESTS = "ms.executionIsolationSemaphoreMaxConcurrentRequests";
}
