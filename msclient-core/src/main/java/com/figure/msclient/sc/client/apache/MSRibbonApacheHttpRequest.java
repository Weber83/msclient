package com.figure.msclient.sc.client.apache;

import com.figure.msclient.sc.client.MSContextAwareRequest;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.BasicHttpEntity;

import java.net.URI;
import java.util.List;

import static com.figure.msclient.sc.client.MSRibbonRequestCustomizer.Runner.customize;


/**
 * Created by chuanbo.wei on 2017/3/18.
 */
public class MSRibbonApacheHttpRequest extends MSContextAwareRequest implements Cloneable {

    public MSRibbonApacheHttpRequest(MSRibbonRequestContext context) {
        super(context);
    }

    public HttpUriRequest toRequest(final RequestConfig requestConfig) {
        final RequestBuilder builder = RequestBuilder.create(this.context.getMethod());
        builder.setUri(this.uri);
        for (final String name : this.context.getHeaders().keySet()) {
            final List<String> values = this.context.getHeaders().get(name);
            for (final String value : values) {
                builder.addHeader(name, value);
            }
        }

        for (final String name : this.context.getParams().keySet()) {
            final List<String> values = this.context.getParams().get(name);
            for (final String value : values) {
                builder.addParameter(name, value);
            }
        }

        if (this.context.getRequestEntity() != null) {
            final BasicHttpEntity entity;
            entity = new BasicHttpEntity();
            entity.setContent(this.context.getRequestEntity());
            // if the entity contentLength isn't set, transfer-encoding will be set
            // to chunked in org.apache.http.protocol.RequestContent. See gh-1042
            if (this.context.getContentLength() != null) {
                entity.setContentLength(this.context.getContentLength());
            } else if ("GET".equals(this.context.getMethod())) {
                entity.setContentLength(0);
            }
            builder.setEntity(entity);
        }

        customize(this.context.getRequestCustomizers(), builder);

        builder.setConfig(requestConfig);
        return builder.build();
    }

    public MSRibbonApacheHttpRequest withNewUri(URI uri) {
        return new MSRibbonApacheHttpRequest(newContext(uri));
    }

}
