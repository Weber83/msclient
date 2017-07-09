package com.figure.msclient.sc.client;

import com.figure.msclient.sc.client.apache.MSRibbonRequestContext;
import com.netflix.client.ClientRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.util.MultiValueMap;

import java.net.URI;

/**
 * Created by chuanbo.wei on 2017/3/19.
 */
public abstract class MSContextAwareRequest extends ClientRequest implements HttpRequest {
    protected final MSRibbonRequestContext context;
    private HttpHeaders httpHeaders;

    public MSContextAwareRequest(MSRibbonRequestContext context) {
        this.context = context;
        MultiValueMap<String, String> headers = context.getHeaders();
        this.httpHeaders = new HttpHeaders();
        for(String key : headers.keySet()) {
            this.httpHeaders.put(key, headers.get(key));
        }
        this.uri = context.uri();
        this.isRetriable = context.getRetryable();
    }

    public MSRibbonRequestContext getContext() {
        return context;
    }

    @Override
    public HttpMethod getMethod() {
        return HttpMethod.valueOf(context.getMethod());
    }

    @Override
    public URI getURI() {
        return this.getUri();
    }

    @Override
    public HttpHeaders getHeaders() {
        return httpHeaders;
    }

    protected MSRibbonRequestContext newContext(URI uri) {
        MSRibbonRequestContext commandContext = new MSRibbonRequestContext(this.context.getServiceId(),
                this.context.getMethod(), uri.toString(), this.context.getRetryable(),
                this.context.getHeaders(), this.context.getParams(), this.context.getRequestEntity(),
                this.context.getRequestCustomizers(), this.context.getContentLength());
        return commandContext;
    }
}
