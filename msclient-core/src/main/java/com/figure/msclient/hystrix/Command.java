package com.figure.msclient.hystrix;

import com.netflix.hystrix.HystrixExecutable;

import java.util.Map;

/**
 * Created by chuanbo.wei on 2017/2/21.
 */
public interface Command extends HystrixExecutable<Map<String, Object>> {
}
