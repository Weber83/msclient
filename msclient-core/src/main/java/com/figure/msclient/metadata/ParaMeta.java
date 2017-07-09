package com.figure.msclient.metadata;

import com.figure.msclient.constant.ParamType;

import java.io.Serializable;

/**
 * Created by chuanbo.wei on 2017/3/14.
 */
public class ParaMeta implements Serializable {
    private String name;

    private ParamType type;

    private Object value;

    public ParaMeta(String name, Object value){
        this(name, value, null);
    }

    public ParaMeta(String name, Object value, ParamType type){
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ParamType getType() {
        return type;
    }

    public void setType(ParamType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
