package com.figure.msclient;

import com.figure.msclient.constant.MSErrorType;

/**
 * Created by chuanbo.wei on 2017/3/11.
 */
public class MSClientException extends Exception {
    private static final long serialVersionUID = -1L;

    private String code;
    private String msg;

    public MSClientException(MSErrorType msErrorType) {
        this(msErrorType.getCode(), msErrorType.getMsg());
    }

    public MSClientException(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void getMsg(String msg) {
        this.msg = msg;
    }
}
