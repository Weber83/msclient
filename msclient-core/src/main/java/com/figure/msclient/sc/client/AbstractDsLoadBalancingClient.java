package com.figure.msclient.sc.client;

import com.netflix.client.AbstractLoadBalancerAwareClient;
import com.netflix.client.IResponse;
import com.netflix.client.RequestSpecificRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;

/**
 * Created by chuanbo.wei on 2017/3/18.
 */
public abstract class AbstractDsLoadBalancingClient <S extends MSContextAwareRequest, T extends IResponse, D> extends
        AbstractLoadBalancerAwareClient<S, T> {

    protected int connectTimeout;

    protected int readTimeout;

    protected boolean secure;

    protected boolean followRedirects;

    protected boolean okToRetryOnAllOperations;

    protected final D delegate;

    protected final IClientConfig config;

    protected final MSServerIntrospector serverIntrospector;

    protected AbstractDsLoadBalancingClient(IClientConfig config, MSServerIntrospector serverIntrospector) {
        super(null);
        this.delegate = createDelegate(config);
        this.config = config;
        this.serverIntrospector = serverIntrospector;
        this.setRetryHandler(RetryHandler.DEFAULT);
        initWithNiwsConfig(config);
    }

    protected AbstractDsLoadBalancingClient(D delegate, IClientConfig config, MSServerIntrospector serverIntrospector) {
        super(null);
        this.delegate = delegate;
        this.config = config;
        this.serverIntrospector = serverIntrospector;
        this.setRetryHandler(RetryHandler.DEFAULT);
        initWithNiwsConfig(config);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        super.initWithNiwsConfig(clientConfig);
        this.connectTimeout = clientConfig.getPropertyAsInteger(
                CommonClientConfigKey.ConnectTimeout,
                DefaultClientConfigImpl.DEFAULT_CONNECT_TIMEOUT);
        this.readTimeout = clientConfig.getPropertyAsInteger(
                CommonClientConfigKey.ReadTimeout,
                DefaultClientConfigImpl.DEFAULT_READ_TIMEOUT);
        this.secure = clientConfig.getPropertyAsBoolean(CommonClientConfigKey.IsSecure,
                false);
        this.followRedirects = clientConfig.getPropertyAsBoolean(
                CommonClientConfigKey.FollowRedirects,
                DefaultClientConfigImpl.DEFAULT_FOLLOW_REDIRECTS);
        this.okToRetryOnAllOperations = clientConfig.getPropertyAsBoolean(
                CommonClientConfigKey.OkToRetryOnAllOperations,
                DefaultClientConfigImpl.DEFAULT_OK_TO_RETRY_ON_ALL_OPERATIONS);
    }

    protected abstract D createDelegate(IClientConfig config);

    public D getDelegate() {
        return this.delegate;
    }

    @Override
    public RequestSpecificRetryHandler getRequestSpecificRetryHandler(
            final S request, final IClientConfig requestConfig) {
        if (this.okToRetryOnAllOperations) {
            return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(),
                    requestConfig);
        }

        if (!request.getContext().getMethod().equals("GET")) {
            return new RequestSpecificRetryHandler(true, false, this.getRetryHandler(),
                    requestConfig);
        }
        else {
            return new RequestSpecificRetryHandler(true, true, this.getRetryHandler(),
                    requestConfig);
        }
    }

    protected boolean isSecure(final IClientConfig config) {
        if(config != null) {
            Boolean result = config.get(CommonClientConfigKey.IsSecure);
            if(result != null) {
                return result;
            }
        }
        return this.secure;
    }
}
