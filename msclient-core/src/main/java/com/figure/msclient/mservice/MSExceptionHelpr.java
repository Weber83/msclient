package com.figure.msclient.mservice;

import com.alibaba.dubbo.rpc.BaseDubboException;
import com.alibaba.dubbo.rpc.BaseDubboServerException;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.service.GenericException;
import com.figure.msclient.MSClientException;
import com.figure.msclient.constant.MSErrorType;
import com.figure.msclient.util.StringUtils;
import com.netflix.client.ClientException;
import com.netflix.hystrix.exception.HystrixRuntimeException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by chuanbo.wei on 2017/3/16.
 */
public class MSExceptionHelpr {

    public static MSClientException generateDsException(Exception exp){
        MSClientException e = null;
        if(exp instanceof RpcException || exp instanceof GenericException){
            e = generateDubboDsException(exp);
        }
        else if(exp instanceof ClientException){
            e = generateSpDsException(exp);
        }

        else if(exp instanceof HystrixRuntimeException){
            e = generateHystrixException(exp);
        }

        else if(exp instanceof NoSuchMethodException){
            e = new MSClientException(MSErrorType.NO_SUCH_METHOD);
        }
        else if(exp instanceof NullPointerException){
            e = new MSClientException(MSErrorType.BIZ_EXP.getCode(), String.format(MSErrorType.BIZ_EXP.getMsg(), "空指针异常"));
        }
        else if(exp instanceof IOException){
            e = new MSClientException(MSErrorType.IO_EXP);
        }
        else if(exp instanceof ExecutionException){
            e = new MSClientException(MSErrorType.NO_SUCH_METHOD);
        }
        else if(exp instanceof TimeoutException){
            e = new MSClientException(MSErrorType.TIMEOUT);
        }
        else {
            e = new MSClientException(MSErrorType.UNKNOWN_EXP);
        }
        return e;
    }

    private static MSClientException generateDubboDsException(Exception exp){
        MSClientException e = null;

        String msg = expPromptMessage(exp);

        if(StringUtils.isNotEmpty(msg)){
            msg = "，提示信息：" + msg;
        }else {
            msg = "";
        }

        if(exp instanceof RpcException){
            RpcException rpcExp = (RpcException)exp;
            if(rpcExp.isNetwork()){
                e = new MSClientException(MSErrorType.NETWORK_EXP.getCode(), MSErrorType.NETWORK_EXP.getMsg() + msg);
            }
            else if(rpcExp.isBiz()){
                e = new MSClientException(MSErrorType.BIZ_EXP.getCode(), MSErrorType.BIZ_EXP.getMsg() + msg);
            }
            else if(rpcExp.isForbidded()){
                e = new MSClientException(MSErrorType.NO_PROVIDER.getCode(), MSErrorType.NO_PROVIDER.getMsg() + msg);
            }
            else if(rpcExp.isSerialization()){
                e = new MSClientException(MSErrorType.SERIALIZATION.getCode(), MSErrorType.SERIALIZATION.getMsg() + msg);
            }
            else if(rpcExp.isTimeout()){
                e = new MSClientException(MSErrorType.TIMEOUT.getCode(), MSErrorType.TIMEOUT.getMsg() + msg);
            }
            else {
                Throwable causeExp = rpcExp.getCause();
                if(causeExp instanceof GenericException){
                    causeExp = causeExp.getCause();
                    if(causeExp instanceof NoSuchMethodException){
                        e = new MSClientException(MSErrorType.NO_SUCH_METHOD.getCode(), MSErrorType.NO_SUCH_METHOD.getMsg() + msg);
                    }
                    else if(causeExp instanceof BaseDubboException){
//                    BaseDubboException baseDubboExp = (BaseDubboException)causeExp;
                        e = new MSClientException(MSErrorType.BIZ_EXP.getCode(), MSErrorType.BIZ_EXP.getMsg() + msg);
                    }
                    else if(causeExp instanceof BaseDubboServerException){
//                    BaseDubboServerException baseDubboServerExp = (BaseDubboServerException)causeExp;
                        e = new MSClientException(MSErrorType.BIZ_EXP.getCode(), MSErrorType.BIZ_EXP.getMsg() + msg);
                    }
                }
                else if(rpcExp.getMessage() != null && rpcExp.getMessage().startsWith("Failed to invoke the method $invoke in the service com.alibaba.dubbo.rpc.service.GenericService. No provider available for the service")){
                    e = new MSClientException(MSErrorType.NO_PROVIDER.getCode(), MSErrorType.NO_PROVIDER.getMsg() + msg);
                }else{
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    rpcExp.printStackTrace(pw);
                    if(sw.toString().indexOf("Caused by: java.lang.NoSuchMethodException") > 0){
                        e = new MSClientException(MSErrorType.NO_SUCH_METHOD.getCode(), MSErrorType.NO_SUCH_METHOD.getMsg() + msg);
                    }else{
                        e = new MSClientException(MSErrorType.UNKNOWN_EXP.getCode(), MSErrorType.UNKNOWN_EXP.getMsg() + msg);
                    }
                }
            }
        }else{
            e = new MSClientException(MSErrorType.UNKNOWN_EXP.getCode(), MSErrorType.UNKNOWN_EXP.getMsg() + msg);
        }
        return e;
    }

    private static MSClientException generateSpDsException(Exception exp){
        MSClientException e = null;
        ClientException rpcExp = (ClientException)exp;
        String msg = expPromptMessage(rpcExp);

        if(StringUtils.isNotEmpty(msg)){
            msg = "，提示信息：" + msg;
        }else {
            msg = "";
        }
        if(ClientException.ErrorType.GENERAL.equals(rpcExp.getErrorType()) || ClientException.ErrorType.CLIENT_THROTTLED.equals(rpcExp.getErrorType())
                || ClientException.ErrorType.CACHE_MISSING.equals(rpcExp.getErrorType()) || ClientException.ErrorType.CONFIGURATION.equals(rpcExp.getErrorType())
                || ClientException.ErrorType.NUMBEROF_RETRIES_EXEEDED.equals(rpcExp.getErrorType()) || ClientException.ErrorType.NUMBEROF_RETRIES_NEXTSERVER_EXCEEDED.equals(rpcExp.getErrorType())){
            msg = MSErrorType.UNKNOWN_EXP.getMsg() + msg;
            e = new MSClientException(MSErrorType.UNKNOWN_EXP.getCode(), msg);
        } else if(ClientException.ErrorType.SOCKET_TIMEOUT_EXCEPTION.equals(rpcExp.getErrorType()) || ClientException.ErrorType.READ_TIMEOUT_EXCEPTION.equals(rpcExp.getErrorType())){
            msg = MSErrorType.TIMEOUT.getMsg() + msg;
            e = new MSClientException(MSErrorType.TIMEOUT);
        } else if(ClientException.ErrorType.CONNECT_EXCEPTION.equals(rpcExp.getErrorType())){
            msg = MSErrorType.NETWORK_EXP.getMsg() + msg;
            e = new MSClientException(MSErrorType.NETWORK_EXP);
        } else if(ClientException.ErrorType.SERVER_THROTTLED.equals(rpcExp.getErrorType()) || ClientException.ErrorType.NO_ROUTE_TO_HOST_EXCEPTION.equals(rpcExp.getErrorType())
                || ClientException.ErrorType.UNKNOWN_HOST_EXCEPTION.equals(rpcExp.getErrorType())){
            //Signals that an error occurred while attempting to connect a socket to a remote address and port, Typically, the remote host cannot be reached because of an intervening firewall,
            //or if an intermediate router is down.
            msg = MSErrorType.NO_PROVIDER.getMsg() + msg;
            e = new MSClientException(MSErrorType.NO_PROVIDER.getCode(), msg);
        }
        return e;
    }

    private static MSClientException generateHystrixException(Exception exp) {
        MSClientException e = null;

        String msg = expPromptMessage(exp);

        if(StringUtils.isNotEmpty(msg)){
            msg = "，提示信息：" + msg;
        }else {
            msg = "";
        }
        e = new MSClientException(MSErrorType.CIRCUIT_BREAKER_EXP.getCode(), msg);
        return e;
    }

    private static String expPromptMessage(Exception exp){
        String expMsg = null;
        if(exp instanceof ClientException){
            ClientException rpcExp = (ClientException)exp;
            expMsg = rpcExp.getErrorMessage();
        }
        if(StringUtils.isEmpty(expMsg)){
            if(StringUtils.isNotEmpty(exp.getMessage())){
                expMsg = exp.getMessage();
            }else {
                Throwable throwable = exp.getCause();
                if(StringUtils.isNotEmpty(throwable.getMessage())){
                    expMsg = throwable.getMessage();
                }
            }
        }
        if(null!=expMsg && expMsg.length()>250){
            expMsg = expMsg.substring(0, 250);
            expMsg += "...";
        }
        return expMsg;
    }
}
