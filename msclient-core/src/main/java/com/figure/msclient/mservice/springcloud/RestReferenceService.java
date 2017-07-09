package com.figure.msclient.mservice.springcloud;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.figure.msclient.MSClientException;
import com.figure.msclient.config.springcloud.EurekaConfig;
import com.figure.msclient.constant.ServiceRpcTypeConstant;
import com.figure.msclient.metadata.ParaMeta;
import com.figure.msclient.mservice.MSExceptionHelpr;
import com.figure.msclient.mservice.InvokeContext;
import com.figure.msclient.mservice.ProxyRequestHelper;
import com.figure.msclient.mservice.ReferenceService;
import com.figure.msclient.sc.client.DefaultDsServerIntrospector;
import com.figure.msclient.sc.client.apache.MSRibbonApacheHttpRequest;
import com.figure.msclient.sc.client.apache.MSRibbonApacheHttpResponse;
import com.figure.msclient.sc.client.apache.MSRibbonLoadBalancingHttpClient;
import com.figure.msclient.util.StringUtils;
import com.netflix.client.ClientException;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.netflix.niws.loadbalancer.DefaultNIWSServerListFilter;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import com.netflix.niws.loadbalancer.EurekaNotificationServerListUpdater;
import com.netflix.niws.loadbalancer.NIWSDiscoveryPing;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by chuanbo.wei on 2017/3/10.
 */
public class RestReferenceService implements ReferenceService {

    private ILoadBalancer loadBalancer;

    private IClientConfig clientConfig;

    private String serviceId;

    private EurekaConfig config;

    private static String RETUREN_DATA = "data";

    public RestReferenceService(String serviceId, EurekaConfig config){
//        super(config);
        this.config = config;
        this.serviceId = serviceId;
    }

    @Override
    public void init() {
        clientConfig = ProxyRequestHelper.getClientConfig(serviceId);
        DiscoveryEnabledNIWSServerList discoveryEnabledNIWSServerList = new DiscoveryEnabledNIWSServerList(clientConfig, config.getProvider());
        loadBalancer = new ZoneAwareLoadBalancer(clientConfig, new ZoneAvoidanceRule(), new NIWSDiscoveryPing(),
                discoveryEnabledNIWSServerList,new DefaultNIWSServerListFilter(), new EurekaNotificationServerListUpdater(config.getProvider()));
    }

    @Override
    public Map<String, Object> invoke(InvokeContext context) throws Exception {
        MSRibbonLoadBalancingHttpClient ribbonLoadBalancingHttpClient = new MSRibbonLoadBalancingHttpClient(clientConfig, new DefaultDsServerIntrospector());
        ribbonLoadBalancingHttpClient.setLoadBalancer(loadBalancer);
        MSRibbonApacheHttpResponse response = null;
//        BufferedReader br = null;
        Map<String, Object> object = null;
        try {
            response = ribbonLoadBalancingHttpClient.executeWithLoadBalancer(new MSRibbonApacheHttpRequest(
                    ProxyRequestHelper.getRibbonCommandContext(serviceId, context.getMethodName(), context.getRequestType(), context.getParaList())));
//            if(200!=response.getStatus()){
//
//            }
            String result = ProxyRequestHelper.getStreamAsString(response.getInputStream(), ProxyRequestHelper.CHARSET_UTF8);
            if(StringUtils.isNotEmpty(result)){
                Object o = JSON.parse(result);
                //处理非统一对象返回
                if(o instanceof JSONArray){
                    object = new JSONObject();
                    object.put(RETUREN_DATA, o);
                } else {
                    object = (Map<String, Object>) o;
                }
            }
            return object;
        } catch (Exception e){
            context.setExp(e);
            throw e;
//            throw MSExceptionHelpr.generateDsException(e);
        } finally {
            if(null!=response){
                response.close();
            }
        }
    }

    @Override
    public void destroy() {
        if(null!=loadBalancer){
            if(loadBalancer instanceof DynamicServerListLoadBalancer){
                ((DynamicServerListLoadBalancer) loadBalancer).shutdown();
            }
            loadBalancer = null;
        }
        if(null!=clientConfig){
            clientConfig = null;
        }
        config = null;
    }

    @Override
    public int rpcType() {
        return ServiceRpcTypeConstant.SPRINGCLOUD.getType();
    }

    public ILoadBalancer getLoadBalancer() {
        return loadBalancer;
    }
}
