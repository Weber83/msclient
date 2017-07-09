package com.figure.msclient.mservice;

/**
 * Created by chuanbo.wei on 2017/3/11.
 */
public class MSInitException extends Exception {
    private static final long serialVersionUID = 21L;

    public MSInitException(String s) {
        super(s);
    }

    public MSInitException() {
        super();
    }

    public MSInitException(Throwable cause) {
        super(cause);
    }
}
