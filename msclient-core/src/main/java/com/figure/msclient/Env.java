package com.figure.msclient;


import com.figure.msclient.config.EnvConfig;

/**
 * Created by chuanbo.wei on 2017/3/10.
 */
public interface Env<T extends EnvConfig> {

    /**
     * 初始化
     */
    public void init();

    /**
     * get default config
     * @return
     */
    public T getDefault();

    /**
     * get gray config
     * @return
     */
    public T getGray();
}
