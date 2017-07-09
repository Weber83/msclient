package com.figure.msclient.sc.client.apache;

import com.figure.msclient.sc.client.MSRibbonRequestCustomizer;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ReflectionUtils;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuanbo.wei on 2017/3/19.
 */
public class MSRibbonRequestContext {

    private String serviceId;

    private String method;

    private String uri;
    private Boolean retryable;

    private MultiValueMap<String, String> headers;

    private MultiValueMap<String, String> params;
    private InputStream requestEntity;

    private List<MSRibbonRequestCustomizer> requestCustomizers;

    private Long contentLength;

    /**
     * Kept for backwards compatibility with Spring Cloud Sleuth 1.x versions
     */
    @Deprecated
    public MSRibbonRequestContext(String serviceId, String method, String uri,
                                  Boolean retryable, MultiValueMap<String, String> headers,
                                  MultiValueMap<String, String> params, InputStream requestEntity) {
        this(serviceId, method, uri, retryable, headers, params, requestEntity,
                new ArrayList<MSRibbonRequestCustomizer>(), null);
    }

    public MSRibbonRequestContext(String serviceId, String method, String uri,
                                  Boolean retryable, MultiValueMap<String, String> headers,
                                  MultiValueMap<String, String> params, InputStream requestEntity,
                                  List<MSRibbonRequestCustomizer> requestCustomizers, Long contentLength) {
        this.serviceId = serviceId;
        this.method = method;
        this.uri = uri;
        this.retryable = retryable;
        this.headers = headers;
        this.params = params;
        this.requestEntity = requestEntity;
        this.requestCustomizers = requestCustomizers;
        this.contentLength = contentLength;
    }

    public URI uri() {
        try {
            return new URI(this.uri);
        }
        catch (URISyntaxException e) {
            ReflectionUtils.rethrowRuntimeException(e);
        }
        return null;
    }

    /**
     * Use getMethod()
     * @return
     */
    @Deprecated
    public String getVerb() {
        return this.method;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Boolean getRetryable() {
        return retryable;
    }

    public MultiValueMap<String, String> getHeaders() {
        return headers;
    }

    public MultiValueMap<String, String> getParams() {
        return params;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public List<MSRibbonRequestCustomizer> getRequestCustomizers() {
        return requestCustomizers;
    }

    public InputStream getRequestEntity() {
        return requestEntity;
    }
}
