package com.figure.msclient.constant;

public enum ServiceRpcTypeConstant {
    //RPC类型，0：dubbo，1：springcloud
	DUBBO(0),
    SPRINGCLOUD(1),
    ;

    private int type;

    ServiceRpcTypeConstant(int type) {
        this.type = type;
    }

    public static boolean isExists(int value){
        for(ServiceRpcTypeConstant constant : ServiceRpcTypeConstant.values()){
            if(constant.getType()==value){
                return true;
            }
        }
        return false;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

}
