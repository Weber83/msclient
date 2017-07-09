package com.figure.msclient.hystrix;


/**
 * Created by chuanbo.wei on 2017/2/21.
 */
public interface MSFallbackProvider<T> {

    /**
     * Provides a fallback response.
     * @return The fallback response.
     */
    public T fallbackResponse();
}
