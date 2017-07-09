package com.figure.msclient.processor;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.figure.msclient.RealMSClient;
import com.figure.msclient.mservice.ReferenceServiceWarp;
import com.zebra.carcloud.dogrobber.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by chuanbo.wei on 2016/12/22.
 */
public class MSMsInfoProcessor extends AbstractDsWebProcessor {
    static Logger logger = LoggerFactory.getLogger(MSMsInfoProcessor.class);

    public final static MSMsInfoProcessor DEFAULT = new MSMsInfoProcessor();

    private MSMsInfoProcessor(){

    }

    @Override
    public void execute(OutputStream outputStream, Request request) throws Exception {
        PrintWriter oos = new PrintWriter(outputStream);

        ConcurrentMap<String, ReferenceServiceWarp> map = RealMSClient.getMsCache();
        Iterator<String> iter = map.keySet().iterator();
        ReferenceServiceWarp rpcService = null;
        String id = null;
        JSONArray list = new JSONArray();
        JSONObject object = null;
        while(iter.hasNext()){
            id = iter.next();
            rpcService = map.get(id);
            object = new JSONObject();
            object.put("id", id);
            object.put("type",rpcService.getService().rpcType());
//            object.put("grayCount",rpcService.getService()..getClass().getName());
            list.add(object);
        }
        oos.write(list.toJSONString());
        oos.flush();
    }

    @Override
    public String desc() {
        return "已注册服务列表";
    }
}
