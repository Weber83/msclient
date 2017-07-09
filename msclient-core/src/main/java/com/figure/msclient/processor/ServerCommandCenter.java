package com.figure.msclient.processor;


import com.zebra.carcloud.dogrobber.CommandCenter;

/**
 *
 */
public class ServerCommandCenter {

    private final static int DOG_PORT = 17179;

    public static void init(){
        CommandCenter commandCenter = CommandCenter.getInstance();
        commandCenter.setPort(DOG_PORT);
        commandCenter.init();

        CommandCenter.registerCommand("msInfo", MSMsInfoProcessor.DEFAULT);
        CommandCenter.registerCommand("total", MSTotalInfoProcessor.DEFAULT);
    }
}
