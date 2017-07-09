package com.figure.msclient.mservice;

import com.figure.msclient.metadata.ParaMeta;

import java.util.List;

/**
 * Created by chuanbo.wei on 2017/3/30.
 */
public class InvokeContext {

    private String methodName;

    private String requestType;

    private List<ParaMeta> paraList;

    private Exception exp;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public List<ParaMeta> getParaList() {
        return paraList;
    }

    public void setParaList(List<ParaMeta> paraList) {
        this.paraList = paraList;
    }

    public Exception getExp() {
        return exp;
    }

    public void setExp(Exception exp) {
        this.exp = exp;
    }
}
