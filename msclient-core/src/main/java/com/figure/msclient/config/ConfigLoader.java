package com.figure.msclient.config;

import com.figure.msclient.config.dubbo.DubboProperties;
import com.figure.msclient.config.springcloud.EurekaClientProperties;
import com.figure.msclient.constant.MSClientModel;

/**
 * Created by chuanbo.wei on 2017/3/10.
 */
public interface ConfigLoader {

    /**
     * @param appName
     * @param msClientModel
     */
    public void init(String appName, MSClientModel msClientModel);

    /**
     * 加载MS配置，
     */
    public MSConfig load();
}
