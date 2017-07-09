package com.figure.msclient.hystrix;

import com.netflix.hystrix.HystrixCommandProperties;

import java.beans.ConstructorProperties;

/**
 * Created by chuanbo.wei on 2017/2/6.
 */
public class HystrixProperties {

    public static final String MSCLIENT_EUREKA = "msclient.eureka.";

    private HystrixCommandProperties.ExecutionIsolationStrategy zopIsolationStrategy;//对依赖调用的隔离策略，分为线程隔离或信号量隔离；默认为线程隔离。

    private Integer circuitBreakerSleepWindowInMilliseconds = 3000;//熔断后，允许再次尝试请求的间隔毫秒数，默认3秒

    private Integer executionTimeoutInMilliseconds = 5000;//调用依赖超时时间毫秒数，如超时则抛出HystrixTimeoutException；executionTimeoutEnabled=true时有效

    private Integer circuitBreakerErrorThresholdPercentage = 50; //失败请求量占总请求量的百分比大于该值时，开启熔断；失败请求包括异常、超时、超线程数拒绝、超信号量拒绝

    private HystrixSemaphore semaphore;

    public HystrixProperties() {
        this.zopIsolationStrategy = HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE;
//        this.zopIsolationStrategy = HystrixCommandProperties.ExecutionIsolationStrategy.THREAD;
        this.semaphore = new HystrixSemaphore();
    }

    public static class HystrixSemaphore {
        private int maxSemaphores = 80;

        public int getMaxSemaphores() {
            return this.maxSemaphores;
        }

        public void setMaxSemaphores(int maxSemaphores) {
            this.maxSemaphores = maxSemaphores;
        }

        public boolean equals(Object o) {
            if(o == this) {
                return true;
            } else if(!(o instanceof HystrixSemaphore)) {
                return false;
            } else {
                HystrixSemaphore other = (HystrixSemaphore)o;
                return !other.canEqual(this)?false:this.getMaxSemaphores() == other.getMaxSemaphores();
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof HystrixSemaphore;
        }

        public int hashCode() {
            boolean PRIME = true;
            byte result = 1;
            int result1 = result * 59 + this.getMaxSemaphores();
            return result1;
        }

        public String toString() {
            return "ZuulProperties.HystrixSemaphore(maxSemaphores=" + this.getMaxSemaphores() + ")";
        }

        @ConstructorProperties({"maxSemaphores"})
        public HystrixSemaphore(int maxSemaphores) {
            this.maxSemaphores = maxSemaphores;
        }

        public HystrixSemaphore() {
        }
    }

    public HystrixCommandProperties.ExecutionIsolationStrategy getZopIsolationStrategy() {
        return zopIsolationStrategy;
    }

    public void setZopIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy zopIsolationStrategy) {
        this.zopIsolationStrategy = zopIsolationStrategy;
    }

    public HystrixSemaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(HystrixSemaphore semaphore) {
        this.semaphore = semaphore;
    }

    public Integer getCircuitBreakerSleepWindowInMilliseconds() {
        return circuitBreakerSleepWindowInMilliseconds;
    }

    public void setCircuitBreakerSleepWindowInMilliseconds(Integer circuitBreakerSleepWindowInMilliseconds) {
        this.circuitBreakerSleepWindowInMilliseconds = circuitBreakerSleepWindowInMilliseconds;
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
}
