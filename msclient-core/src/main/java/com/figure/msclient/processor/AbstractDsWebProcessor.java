package com.figure.msclient.processor;

import com.zebra.carcloud.dogrobber.ICommandProcessor;
import com.zebra.carcloud.dogrobber.Request;

import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by chuanbo.wei on 2016/12/22.
 */
public abstract class AbstractDsWebProcessor implements ICommandProcessor {

    @Override
    public void execute(OutputStream outputStream, Request request, Charset charset) throws Exception {

    }
}
