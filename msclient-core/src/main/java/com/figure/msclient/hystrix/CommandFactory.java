package com.figure.msclient.hystrix;


import com.figure.msclient.mservice.InvokeContext;
import com.figure.msclient.mservice.ReferenceService;

/**
 * Created by chuanbo.wei on 2017/2/21.
 */
public interface CommandFactory<T extends Command> {

    T create(ReferenceService service, InvokeContext context);
}
