package com.figure.msclient.constant;

/**
 * Created by chuanbo.wei on 2017/3/13.
 */
public enum MSErrorType {
    UNKNOWN_EXP("0001", "未知服务异常"),
    NETWORK_EXP("0002", "网络异常"),
    NO_PROVIDER("0003", "无服务提供者，请确认服务是否启动、注册或被禁止调用"),
    SERIALIZATION("0004", "序列化错误"),
    TIMEOUT("0005", "服务超时"),
    NO_SUCH_METHOD("0006", "方法不存在或方法参数、类型不匹配"),
    BIZ_EXP("0007", "业务服务异常"),
    CIRCUIT_BREAKER_EXP("0008", "熔断机制"),
//    HTTP_RESPONSE_EXP("0009", "http状态码为%s"),
//    GATEWAY_CIRCUIT_BREAKER_EXP("0010", "提示信息：%s"),
    IO_EXP("0011", "I/O异常"),
    MS_NOT_INIT_EXP("0012", "无服务信息，确认服务是否登记成功"),
    ;

    private String code;
    private String msg;

    MSErrorType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
