package com.figure.msclient.sc.client.apache;

import com.figure.msclient.sc.client.AbstractDsLoadBalancingClient;
import com.figure.msclient.sc.client.MSServerIntrospector;
import com.figure.msclient.sc.util.RibbonUtils;
import com.netflix.client.RequestSpecificRetryHandler;
import com.netflix.client.RetryHandler;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.Server;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * Created by chuanbo.wei on 2017/3/18.
 */
public class MSRibbonLoadBalancingHttpClient extends
        AbstractDsLoadBalancingClient<MSRibbonApacheHttpRequest, MSRibbonApacheHttpResponse, HttpClient> {

    public MSRibbonLoadBalancingHttpClient(IClientConfig config, MSServerIntrospector serverIntrospector) {
        super(config, serverIntrospector);
    }

    public MSRibbonLoadBalancingHttpClient(HttpClient delegate, IClientConfig config, MSServerIntrospector serverIntrospector) {
        super(delegate, config, serverIntrospector);
    }

    protected HttpClient createDelegate(IClientConfig config) {
        return HttpClientBuilder.create()
                // already defaults to 0 in builder, so resetting to 0 won't hurt
                .setMaxConnTotal(config.getPropertyAsInteger(CommonClientConfigKey.MaxTotalConnections, 0))
                        // already defaults to 0 in builder, so resetting to 0 won't hurt
                .setMaxConnPerRoute(config.getPropertyAsInteger(CommonClientConfigKey.MaxConnectionsPerHost, 0))
                .disableCookieManagement()
                .useSystemProperties() // for proxy
                .build();
    }

    @Override
    public MSRibbonApacheHttpResponse execute(MSRibbonApacheHttpRequest request,
                                            final IClientConfig configOverride) throws Exception {
        final RequestConfig.Builder builder = RequestConfig.custom();
        IClientConfig config = configOverride != null ? configOverride : this.config;
        builder.setConnectTimeout(config.get(
                CommonClientConfigKey.ConnectTimeout, this.connectTimeout));
        builder.setSocketTimeout(config.get(
                CommonClientConfigKey.ReadTimeout, this.readTimeout));
        builder.setRedirectsEnabled(config.get(
                CommonClientConfigKey.FollowRedirects, this.followRedirects));

        final RequestConfig requestConfig = builder.build();
        if (isSecure(configOverride)) {
            final URI secureUri = UriComponentsBuilder.fromUri(request.getUri())
                    .scheme("https").build().toUri();
            request = request.withNewUri(secureUri);
        }
        final HttpUriRequest httpUriRequest = request.toRequest(requestConfig);
        final HttpResponse httpResponse = this.delegate.execute(httpUriRequest);
        return new MSRibbonApacheHttpResponse(httpResponse, httpUriRequest.getURI());
    }

    @Override
    public URI reconstructURIWithServer(Server server, URI original) {
        URI uri = RibbonUtils.updateToHttpsIfNeeded(original, this.config, this.serverIntrospector, server);
        return super.reconstructURIWithServer(server, uri);
    }

    @Override
    public RequestSpecificRetryHandler getRequestSpecificRetryHandler(MSRibbonApacheHttpRequest request, IClientConfig requestConfig) {
        return new RequestSpecificRetryHandler(false, false, RetryHandler.DEFAULT, null);
    }
}
