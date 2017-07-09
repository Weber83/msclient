package com.figure.msclient.config.ms;

import com.figure.msclient.metadata.ReferenceBean;

import java.util.List;

/**
 * Created by chuanbo.wei on 2017/3/20.
 */
public class MsReferenceManage {

    private final static MsReferenceManage instance = new MsReferenceManage();

    private MsReferenceConfig msReferenceConfig;

    private MsReferenceManage(){}

    private volatile static boolean inited = false;

    private List<ReferenceBean> beans;

    public static MsReferenceManage getInstance(){
        return instance;
    }

    public void init(){
        if(inited)
            return;
        msReferenceConfig = new DefaultMsReferenceConfig();
        beans = msReferenceConfig.load();

        inited = true;
    }

    public List<ReferenceBean> getBeans() {
        return beans;
    }

    public void setBeans(List<ReferenceBean> beans) {
        this.beans = beans;
    }
}
