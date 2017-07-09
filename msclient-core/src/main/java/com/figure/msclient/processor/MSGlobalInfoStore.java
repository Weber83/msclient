package com.figure.msclient.processor;

import com.figure.msclient.config.MSConfig;

/**
 * Created by chuanbo.wei on 2017/3/30.
 */
public class MSGlobalInfoStore {
    private static MSGlobalInfoStore instance = new MSGlobalInfoStore();

    private MSGlobalInfoStore(){

    }

    public static MSGlobalInfoStore getInstance(){
        return instance;
    }

    private MSConfig msConfig;


    public MSConfig getDsConfig() {
        return msConfig;
    }

    public void setDsConfig(MSConfig msConfig) {
        this.msConfig = msConfig;
    }
}
