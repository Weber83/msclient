package com.figure.msclient.metadata;

/**
 * Created by chuanbo.wei on 2017/3/20.
 */
public class ReferenceBean {

    public ReferenceBean(){

    }

    public ReferenceBean(String id, String serviceId, int protocolType) {
        this(id, serviceId, null, null, protocolType);
    }

    public ReferenceBean(String id, String serviceId, String version, int protocolType) {
        this(id, serviceId, version, null, protocolType);
    }

    public ReferenceBean(String id, String serviceId, String version, String group, int protocolType) {
        this(id, serviceId, version, group, protocolType, null);
    }

    public ReferenceBean(String id, String serviceId, String version, String group, int protocolType, String desc) {
        this.id = id;
        this.serviceId = serviceId;
        this.version = version;
        this.group = group;
        this.protocolType = protocolType;
        this.desc = desc;
    }

    private String id;

    private String serviceId;

    private String version;

    private String group;

    private int protocolType;

    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
