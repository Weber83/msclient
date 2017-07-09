package com.figure.msclient.mservice;

import com.alibaba.dubbo.rpc.BaseDubboException;
import com.alibaba.dubbo.rpc.RpcException;
import com.figure.msclient.constant.HttpRequestMethod;
import com.figure.msclient.metadata.ParaMeta;
import com.figure.msclient.sc.client.MSRibbonRequestCustomizer;
import com.figure.msclient.sc.client.apache.MSRibbonRequestContext;
import com.figure.msclient.util.StringUtils;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.DefaultClientConfigImpl;
import com.netflix.client.config.IClientConfig;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by chuanbo.wei on 2017/3/13.
 */
public class ProxyRequestHelper {

    /** UTF-8字符集 **/
    public static final String CHARSET_UTF8 = "UTF-8";

    /** GBK字符集 **/
    public static final String CHARSET_GBK = "GBK";
    private static final String DEFAULT_CHARSET = CHARSET_UTF8;

    public static MultiValueMap<String, String> buildRequestQueryParams(Map<String, Object> map) {
//        Map map = HTTPRequestUtils.getInstance().getQueryParams();
//        Map<String, Object> map = context.getServiceParamMap();
        LinkedMultiValueMap params = new LinkedMultiValueMap();
        if(map == null) {
            return params;
        } else {
            Iterator<String> var4 = map.keySet().iterator();

            while(var4.hasNext()) {
                String key = var4.next();

//                Iterator var6 = ((List)map.get(key)).iterator();
//                while(var6.hasNext()) {
//                    String value = (String)var6.next();
//                    params.add(key, value);
//                }

                params.add(key, map.get(key));
            }

            return params;
        }
    }

    public static MultiValueMap<String, String> buildRequestQueryParams(List<ParaMeta> paraList) {
        LinkedMultiValueMap params = new LinkedMultiValueMap();
        if(paraList == null || paraList.size()==0) {
            return params;
        } else {
            for (ParaMeta entry : paraList) {
                params.add(entry.getName(), entry.getValue());
            }
            return params;
        }
    }

    public static MultiValueMap<String, String> buildRequestHeaders() {
//        RequestContext context = RequestContext.getCurrentContext();
        HttpHeaders headers = new HttpHeaders();
//        Enumeration headerNames = request.getHeaderNames();
//        String header;
//        if(headerNames != null) {
//            while(headerNames.hasMoreElements()) {
//                String zuulRequestHeaders = (String)headerNames.nextElement();
//                if(this.isIncludedHeader(zuulRequestHeaders)) {
//                    Enumeration values = request.getHeaders(zuulRequestHeaders);
//
//                    while(values.hasMoreElements()) {
//                        header = (String)values.nextElement();
//                        headers.add(zuulRequestHeaders, header);
//                    }
//                }
//            }
//        }
//
//        Map zuulRequestHeaders1 = context.getZuulRequestHeaders();
//        Iterator values1 = zuulRequestHeaders1.keySet().iterator();
//
//        while(values1.hasNext()) {
//            header = (String)values1.next();
//            headers.set(header, zuulRequestHeaders1.get(header));
//        }

        headers.set("Accept-Encoding", "gzip");
        return headers;
    }


    public static String buildQuery(Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        Set<Map.Entry<String, Object>> entries = params.entrySet();
        boolean hasParam = false;

        for (Map.Entry<String, Object> entry : entries) {
            String name = entry.getKey();
            Object value = entry.getValue();
            if(value == null)
                value = "";
            if (hasParam) {
                query.append("&");
            } else {
                hasParam = true;
            }

            try {
                query.append(name).append("=").append(URLEncoder.encode(value.toString(), DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return query.toString();
    }

    public static String buildQuery(List<ParaMeta> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder query = new StringBuilder();
        boolean hasParam = false;
        String name = null;
        Object value = null;
        for (ParaMeta entry : params) {
            name = entry.getName();
            value = entry.getValue();

            if(value == null)
                value = "";
            if (hasParam) {
                query.append("&");
            } else {
                hasParam = true;
            }

            try {
                query.append(name).append("=").append(URLEncoder.encode(value.toString(), DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return query.toString();
    }

    public static String getStreamAsString(InputStream stream, String charset) throws IOException {
        try {
            Reader reader = new InputStreamReader(stream, charset);
            StringBuilder response = new StringBuilder();

            final char[] buff = new char[1024];
            int read = 0;
            while ((read = reader.read(buff)) > 0) {
                response.append(buff, 0, read);
            }

            return response.toString();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

    public static String getResponseCharset(String ctype) {
        String charset = DEFAULT_CHARSET;

        if (StringUtils.isNotEmpty(ctype)) {
            String[] params = ctype.split(";");
            for (String param : params) {
                param = param.trim();
                if (param.startsWith("charset")) {
                    String[] pair = param.split("=", 2);
                    if (pair.length == 2) {
                        if (StringUtils.isNotEmpty(pair[1])) {
                            charset = pair[1].trim();
                        }
                    }
                    break;
                }
            }
        }

        return charset;
    }

    public static MSRibbonRequestContext getRibbonCommandContext(String serviceId, String methodName, String requestType, List<ParaMeta> paraList){
        MultiValueMap headers = buildRequestHeaders();
        MultiValueMap params = null;
        String path = methodName;
        if(StringUtils.isEmpty(requestType)){
            requestType = HttpRequestMethod.GET.getType();
        }
        if(HttpRequestMethod.GET.getType().equalsIgnoreCase(requestType)) {
            path += "?" + buildQuery(paraList);
            params = new LinkedMultiValueMap();
        }else {
            params = buildRequestQueryParams(paraList);
        }
        return new MSRibbonRequestContext(serviceId, requestType, path, Boolean.FALSE, headers,
                params, null, new ArrayList<MSRibbonRequestCustomizer>(), null);
    }

    public static IClientConfig getClientConfig(String serviceId){
        IClientConfig clientConfig = new DefaultClientConfigImpl();
        clientConfig.loadDefaultValues();
        clientConfig.set(CommonClientConfigKey.DeploymentContextBasedVipAddresses,serviceId.toUpperCase());
        return clientConfig;
    }

    private static final String CLASS_FLAG = "class";

    public static void markClassTag(Object obj){
        if(null==obj || obj.getClass().isPrimitive())
            return;
        if(obj instanceof Map){
            mapClassMark((Map) obj);
        }else if(obj instanceof Collection){
            listClassMark((Collection) obj);
        }

    }

    private static void mapClassMark(Map m){
        if(null==m || m.size()<=0)
            return;
        m.remove(CLASS_FLAG);
        Object obj = null;
        for (Iterator e = m.keySet().iterator();e.hasNext();){
            obj = m.get(e.next());
            if(null==obj)
                continue;
            if(obj instanceof Map){
                mapClassMark((Map)obj);
            }else if(obj instanceof Collection){
                listClassMark((Collection) obj);
            }
        }
    }

    private static void listClassMark(Collection c){
        if(null==c || c.size()<=0)
            return;
        Iterator iterator = c.iterator();
        Object o = null;
        while (iterator.hasNext()){
            o = iterator.next();
            if(null==o)
                continue;
            if(o instanceof Map){
                mapClassMark((Map)o);
            }else if(o instanceof Collection){
                listClassMark((Collection) o);
            }
        }
    }

    public static boolean checkBizExp(Exception exp){
        if(exp instanceof BaseDubboException){
            return true;
        }

        if(exp instanceof RpcException){
            Throwable throwable = exp.getCause();
            if(throwable instanceof BaseDubboException){
                return true;
            }
            throwable = throwable.getCause();
            if(throwable instanceof BaseDubboException){
                return true;
            }
        }
        return false;
    }
}
