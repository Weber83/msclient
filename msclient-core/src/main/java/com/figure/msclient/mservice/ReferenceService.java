package com.figure.msclient.mservice;

import com.figure.msclient.MSClientException;
import com.figure.msclient.metadata.ParaMeta;

import java.util.List;
import java.util.Map;

/**
 * Created by chuanbo.wei on 2017/3/10.
 */
public interface ReferenceService {
    /**
     * RPC service 初始化
     */
    public void init();

    /**
     * RPC, invoke service
     * @param context
     */
    public Map<String, Object> invoke(InvokeContext context) throws Exception;

    /**
     * rpc service destory
     */
    public void destroy();

    /**
     * rpc类型
     * @return
     */
    public int rpcType();
}
