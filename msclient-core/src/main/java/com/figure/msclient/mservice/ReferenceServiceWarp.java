package com.figure.msclient.mservice;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 某个微服务被API引用计数包装类，主要解决不同API引用相同微服务时，服务的更新销毁等情况
 * Created by chuanbo.wei on 2017/3/13.
 */
public class ReferenceServiceWarp {
    private volatile AtomicInteger referenceCount;

    private ReferenceService service;

    public ReferenceServiceWarp(ReferenceService service){
        this.service = service;
        referenceCount = new AtomicInteger(1);
    }

    public AtomicInteger getReferenceCount() {
        return referenceCount;
    }

    public int addReferenceCount() {
        return referenceCount.incrementAndGet();
    }

    public int decrement() {
        return referenceCount.decrementAndGet();
    }

    public ReferenceService getService() {
        return service;
    }

    public void setService(ReferenceService service) {
        this.service = service;
    }
}
