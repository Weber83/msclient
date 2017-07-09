package com.figure.msclient.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.figure.msclient.RealMSClient;
import com.figure.msclient.config.MSConfig;
import com.figure.msclient.mservice.ReferenceService;
import com.figure.msclient.mservice.ReferenceServiceWarp;
import com.figure.msclient.mservice.dubbo.DubboReferenceService;
import com.figure.msclient.mservice.springcloud.RestReferenceService;
import com.netflix.loadbalancer.Server;
import com.zebra.carcloud.dogrobber.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by chuanbo.wei on 2016/12/22.
 */
public class MSTotalInfoProcessor extends AbstractDsWebProcessor {
    static Logger logger = LoggerFactory.getLogger(MSTotalInfoProcessor.class);

    public final static MSTotalInfoProcessor DEFAULT = new MSTotalInfoProcessor();

    private MSTotalInfoProcessor(){

    }


    @Override
    public void execute(OutputStream outputStream, Request request) throws Exception {
        PrintWriter oos = new PrintWriter(outputStream);
        JSONObject overall = new JSONObject();
        MSConfig msConfig = MSGlobalInfoStore.getInstance().getDsConfig();
        overall.put("msConfig", getConfig(msConfig));

        ConcurrentMap<String, ReferenceServiceWarp> map = RealMSClient.getMsCache();
        Set<String> ifKeySet =  RealMSClient.getIfKeyCache();
        overall.put("ifKeySet", getIfKeySet(ifKeySet));

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
            object.put("type", rpcService.getService().rpcType());
            object.put("servers", getReference(rpcService.getService()));
            list.add(object);
        }
        overall.put("references", list);
        oos.write(overall.toJSONString());
        oos.flush();
    }

    @Override
    public String desc() {
        return "MSClient全局信息";
    }

    private Object getConfig(MSConfig msConfig){
        if(null!=msConfig){
            return JSON.toJSON(msConfig);
        }
        return null;
    }

    private Object getIfKeySet(Set<String> ifKeySet){
        if(null!=ifKeySet){
            return JSON.toJSON(ifKeySet);
        }
        return null;
    }

    private Object getReference(ReferenceService service){
        if(null==service){
            return null;
        }
        JSONArray list = new JSONArray();
        JSONObject object = null;
        if(service instanceof DubboReferenceService){
            DubboReferenceService s = (DubboReferenceService) service;
            object = new JSONObject();
            object.put("interface", s.getInterface());
            object.put("version", s.getVersion());
            object.put("group", s.getGroup());
            object.put("timeout", s.getTimeout());
            list.add(object);

        }else if(service instanceof RestReferenceService){
            RestReferenceService s = (RestReferenceService) service;
            List<Server> serverList = s.getLoadBalancer().getAllServers();
            if(null!=serverList && serverList.size()>0){
                for (Server server : serverList){
                    object = new JSONObject();
                    object.put("zone", server.getZone());
                    object.put("address", server.getId());
                    object.put("isAlive", server.isAlive());
                    object.put("isReady", server.isReadyToServe());
                    list.add(object);
                }
            }
        }
        return list;
    }
}
