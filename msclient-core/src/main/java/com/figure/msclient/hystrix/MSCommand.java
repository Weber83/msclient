package com.figure.msclient.hystrix;

import com.figure.msclient.mservice.InvokeContext;
import com.figure.msclient.mservice.ReferenceService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.Map;

/**
 * Created by chuanbo.wei on 2017/2/21.
 */
public class MSCommand extends HystrixCommand<Map<String, Object>> implements Command{

    protected ReferenceService service;

    protected MSFallbackProvider<Map<String, Object>> fallbackProvider;

    protected InvokeContext context;

    public MSCommand(String commandKey, ReferenceService service, InvokeContext context, HystrixProperties hystrixProperties) {
        this(commandKey, service, context, hystrixProperties, null);
    }

    public MSCommand(String commandKey, ReferenceService service, InvokeContext context, HystrixProperties hystrixProperties,
                     MSFallbackProvider fallbackProvider) {
        super(getSetter(commandKey, context, hystrixProperties));
        this.service = service;
        this.context = context;
        this.fallbackProvider = fallbackProvider;
    }

    @Override
    protected Map<String, Object> run() throws Exception {
        return service.invoke(context);
    }

    protected static Setter getSetter(final String commandKey, InvokeContext context,
                                      HystrixProperties hystrixProperties) {

        // @formatter:off
        final HystrixCommandProperties.Setter setter = HystrixCommandProperties.Setter()
                .withRequestCacheEnabled(false)//Whether request caching is enabled.
                .withExecutionTimeoutEnabled(true)
                .withFallbackEnabled(false)
                .withExecutionIsolationStrategy(hystrixProperties.getZopIsolationStrategy())
//                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                .withCircuitBreakerErrorThresholdPercentage(hystrixProperties.getCircuitBreakerErrorThresholdPercentage())
                .withExecutionIsolationSemaphoreMaxConcurrentRequests(hystrixProperties.getSemaphore().getMaxSemaphores())
                .withCircuitBreakerSleepWindowInMilliseconds(hystrixProperties.getCircuitBreakerSleepWindowInMilliseconds())
                .withExecutionTimeoutInMilliseconds(hystrixProperties.getExecutionTimeoutInMilliseconds());

        return Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ZopCommand"))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                .andCommandPropertiesDefaults(setter);
        // @formatter:on
    }

    @Override
    protected Map<String, Object> getFallback() {
        if(fallbackProvider != null) {
            return fallbackProvider.fallbackResponse();
        }
        return super.getFallback();
    }
}
