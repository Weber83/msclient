package com.figure.msclient.constant;

public enum HttpRequestMethod {

	GET("get"),
    POST("post"),
    ;

    private String type;

    HttpRequestMethod(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
