package com.figure.msclient.test;


import com.figure.msclient.MSClient;
import com.figure.msclient.MSClientException;
import com.figure.msclient.config.loader.PropertiesConfigLoader;
import com.figure.msclient.constant.HttpRequestMethod;
import com.figure.msclient.constant.MSClientModel;
import com.figure.msclient.constant.ParamType;
import com.figure.msclient.constant.ServiceRpcTypeConstant;
import com.figure.msclient.metadata.ParaMeta;
import com.figure.msclient.mservice.MSInitException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuanbo.wei on 2017/3/11.
 */
public class MSClientHystrixTest {
    MSClient dsClient = null;

    public void init(){
        dsClient = MSClient.builder().appName("user").model(MSClientModel.FULL).configLoader(new PropertiesConfigLoader()).build();

//        dsClient = MSClient.builder().appName("openapi-gateway").model(MSClientModel.SPRINGCLOUD).configLoader(new PropertiesConfigLoader()).build();
    }

//    @Test
    public void MSClientSpTest(){
        //初始化DSClint
        init();

        String apiName = "figure.mpauth.qrcode.validate";
        String version = "1.0";
        String serviceId = "user-service";
//        String methodName = "/zid/mpauth/1.0/mobileVerifyQrCode";

        String methodName = "/zapi/api/1.0/queryApiInParamByApiId";

//        String methodName = "/zapi/api/1.0/getApiByUniqueName";

        String serviceVersion = null;
        String serviceGroup = null;
        int protocolType = ServiceRpcTypeConstant.SPRINGCLOUD.getType();
        String requestType = HttpRequestMethod.GET.getType();
        boolean isGray =false;
        //登记服务引用
        try {
//            dsClient.registry(apiName, version, serviceId, serviceVersion, serviceGroup, protocolType, isGray);
            dsClient.registry("userService", serviceId, serviceVersion, serviceGroup, protocolType);
            dsClient.registry("devService", "dev-service", serviceVersion, serviceGroup, protocolType);
            dsClient.registry("apiService", "api-service", serviceVersion, serviceGroup, protocolType);

        } catch (MSInitException e) {
            e.printStackTrace();
        }

        List<ParaMeta> paraList = new ArrayList<ParaMeta>();
//        paraList.add(new ParaMeta("token", "A5BB389B0D8B82E1E16A1EDE84B92C84"));
//        paraList.add(new ParaMeta("qrcode", "1"));

        paraList.add(new ParaMeta("apiId", "9b380a77353d472998c60cf81e9531de"));

//        paraList.add(new ParaMeta("uniqueName", "ebanma.cloud.zid.account.get"));

        Object result = null;
        //调用
        int i = 2000;
        while(i>0){
            try {
//                result = dsClient.invoke("zidService", methodName, requestType, paraList);
//                result = dsClient.invoke("zapiService", methodName, requestType, paraList);
                result = dsClient.invoke("apiService", methodName, requestType, paraList);
                System.out.println("Result>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + result);

                result = dsClient.invoke("apiService", methodName, requestType, paraList);
                System.out.println("Result>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + result);

                result = dsClient.invoke("apiService", methodName, requestType, paraList);
                System.out.println("Result>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + result);

                i--;
            } catch (MSClientException e) {
                System.out.println("Error result>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + e.getCode() + ":"+ e.getMsg());
                e.printStackTrace();
                i--;
            }
        }

        try {
            Thread.sleep(1000*60*3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //引用服务销毁
//        dsClient.destroy(serviceId, serviceVersion, serviceGroup, protocolType, isGray);
    }


//        @Test
    public void MSClientSpTest1(){
        //初始化DSClint
        init();

        int protocolType = ServiceRpcTypeConstant.SPRINGCLOUD.getType();
        String serviceVersion = null;
        String serviceGroup = null;
        boolean isGray =false;
        //登记服务引用
        try {
//            dsClient.registry(apiName, version, serviceId, serviceVersion, serviceGroup, protocolType, isGray);
            dsClient.registry("userService", "user-service", serviceVersion, serviceGroup, protocolType);
        } catch (MSInitException e) {
            e.printStackTrace();
        }

        String methodName = "/user/identify/1.0/login";
        String requestType = HttpRequestMethod.GET.getType();
        List<ParaMeta> paraList = new ArrayList<ParaMeta>();
        paraList.add(new ParaMeta("mobile", "A5BB389B0D8B82E1E16A1EDE84B92C84"));
        paraList.add(new ParaMeta("pwd",  "1"));
        paraList.add(new ParaMeta("appKey",  "1008"));
        paraList.add(new ParaMeta("deviceType",  "1"));

        Object result = null;
        try {
            //调用
//            result = dsClient.invoke(serviceId, methodName, serviceVersion, serviceGroup, protocolType, requestType, isGray, paraList);
            result = dsClient.invoke("userService", methodName, requestType, paraList);
            System.out.println("Result>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + result);
        } catch (MSClientException e) {
            System.out.println("Error result>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + e.getCode() + ":"+ e.getMsg());
            e.printStackTrace();
        }

        //引用服务销毁
//        dsClient.destroy(serviceId, serviceVersion, serviceGroup, protocolType, isGray);
    }

//    @Test
    public void MSClientSpTest2(){
        //初始化DSClint
        init();

        int protocolType = ServiceRpcTypeConstant.SPRINGCLOUD.getType();
        String serviceVersion = null;
        String serviceGroup = null;
        boolean isGray =false;
        //登记服务引用
        try {
//            dsClient.registry(apiName, version, serviceId, serviceVersion, serviceGroup, protocolType, isGray);
            dsClient.registry("userService", "user-service", serviceVersion, serviceGroup, protocolType);
        } catch (MSInitException e) {
            e.printStackTrace();
        }

        String methodName = "/zid/accountToken/1.0/validateToken";
        String requestType = HttpRequestMethod.GET.getType();
        List<ParaMeta> paraList = new ArrayList<ParaMeta>();
        paraList.add(new ParaMeta("token", "11111111-7475-4f81-aa82-2b616a561590"));

        Object result = null;
        int i = 2000;
        while(i>0){
            try {
                //调用
//            result = dsClient.invoke(serviceId, methodName, serviceVersion, serviceGroup, protocolType, requestType, isGray, paraList);
                result = dsClient.invoke("userService", methodName, requestType, paraList);
                System.out.println("Result>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + result);
            } catch (MSClientException e) {
                System.out.println("Error result>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + e.getCode() + ":"+ e.getMsg());
                e.printStackTrace();
            }
            i--;
        }

        //引用服务销毁
//        dsClient.destroy(serviceId, serviceVersion, serviceGroup, protocolType, isGray);
    }

//        @Test
    public void MSClientDubboTest(){
        //初始化DSClint
        init();

        String apiName = "figure.user.token.get";
        String version = "1.0";
        String serviceId = "figure.user.dubbo.IBaseUserTokenService";
        String methodName = "getPlatformTokenDetailInfo";
        String serviceVersion = "1.0";
        String serviceGroup = null;
        int protocolType = ServiceRpcTypeConstant.DUBBO.getType();
        String requestType = null;
        boolean isGray =false;
        //登记服务引用
        try {
//            dsClient.registry(apiName, version, serviceId, serviceVersion, serviceGroup, protocolType, isGray);
            dsClient.registry("usercenter", serviceId, serviceVersion, serviceGroup, protocolType);
        } catch (MSInitException e) {
            e.printStackTrace();
        }

        List<ParaMeta > paraList = new ArrayList<ParaMeta>();
        paraList.add(new ParaMeta("platformToken", "11111111-7475-4f81-aa82-2b616a561590", ParamType.STRING));

        Object result = null;
        try {
            //调用
//            result = dsClient.invoke(serviceId, methodName, serviceVersion, serviceGroup, protocolType, requestType, isGray, paraList);
            result = dsClient.invoke("usercenter", methodName, requestType, paraList);
            System.out.println("Result>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + result);
        } catch (MSClientException e) {
            System.out.println("Error result>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + e.getCode() + ":"+ e.getMsg());
            e.printStackTrace();
        }

        //引用服务销毁
//        dsClient.destroy(serviceId, serviceVersion, serviceGroup, protocolType, isGray);
    }
}
