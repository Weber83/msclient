package com.figure.msclient;

import com.figure.msclient.config.ConfigLoader;
import com.figure.msclient.config.loader.PropertiesConfigLoader;
import com.figure.msclient.constant.MSClientModel;
import com.figure.msclient.metadata.ParaMeta;
import com.figure.msclient.mservice.MSInitException;
import com.figure.msclient.util.Assert;
import com.figure.msclient.util.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by chuanbo.wei on 2017/3/10.
 */
public abstract class MSClient {

    /**
     * 服务登记，MSClient会向服务注册中心获取服务信息，初始化，并根据服务的唯一标识，缓存服务引用；
     * MSClient保持与服务注册中心交互，以更新服务的变化；
     * 服务唯一标识：
     * dubbo服务为 serviceId:serviceVersion:serviceGroup，serviceVersion、serviceGroup可以为空，都为空时，唯一标识为serviceId
     * springcloud时为 serviceId:serviceVersion，serviceVersion
//     * @param apiName   API名称
//     * @param version   API版本号
     * @param serviceId dubbo服务时，该值为借口类全路径，如：com.zebra.carcloud.device.iface.IDeviceService，springcloud时为应用名，对应spring.application.name配置
     * @param serviceVersion    服务接口版本号
     * @param serviceGroup  服务接口分组，只dubbo服务使用
     * @param protocolType  RPC类型，0：dubbo，1：springcloud，ServiceRpcTypeConstant类定义
//     * @param isGray    是否灰度服务
     * @throws com.figure.msclient.constant.MSClientModel
     */
    public abstract void registry(String id, String serviceId, String serviceVersion, String serviceGroup, int protocolType) throws MSInitException;

    /**
     * 服务调用，返回类型为Map<String, Object>
//     * @param serviceId dubbo服务时，该值为借口类全路径，如：com.zebra.carcloud.device.iface.IDeviceService，spring cloud时为应用名，对应spring.application.name配置
     * @param methodName    调用服务接口的方法名，dubbo时对应接口的具体方法名，spring cloud时为RequestMapping注解的值，如/zid/identify/login/1.0
//     * @param serviceVersion    服务接口版本号
//     * @param serviceGroup  服务接口分组，只dubbo服务使用
//     * @param protocolType  RPC类型，0：dubbo，1：springcloud，ServiceRpcTypeConstant类定义
     * @param requestType   HTTP请求方法，get或post，HttpRequestMethod类定义
//     * @param isGray    是否灰度服务
     * @param paraList  接口入参，dubbo服务必须按API入参定义顺序存放
     * @return  Map<String, Object>
     * @throws com.figure.msclient.MSClientException
     */
    public abstract Map<String, Object> invoke(String id, String methodName, String requestType, List<ParaMeta> paraList) throws MSClientException;

    /**
     * 服务销毁
//     * @param serviceId dubbo服务时，该值为借口类全路径，如：com.zebra.carcloud.device.iface.IDeviceService，springcloud时为应用名，对应spring.application.name配置
//     * @param serviceVersion    服务接口版本号
//     * @param serviceGroup  服务接口分组，只dubbo服务使用
//     * @param protocolType  RPC类型，0：dubbo，1：springcloud，ServiceRpcTypeConstant类定义
//     * @param isGray    是否灰度服务
     */
    public abstract void destroy(String id);


    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        //应用名称，对应配置中心的应用名
        private String appName;


        private ConfigLoader configLoader = new PropertiesConfigLoader();

        private MSClientModel msClientModel = MSClientModel.FULL;

        public Builder appName(String appName){
            this.appName = appName;
            return this;
        }

        public Builder configLoader(ConfigLoader configLoader){
            this.configLoader = configLoader;
            return this;
        }

        public Builder model(MSClientModel msClientModel){
            this.msClientModel = msClientModel;
            return this;
        }

        public MSClient build(){
            Assert.isTrue(StringUtils.isNotEmpty(appName),"appName 不能为null");
            Assert.notNull(configLoader, "配置加载器不能为null");
            return new RealMSClient(appName, configLoader, msClientModel);
        }
    }
}
