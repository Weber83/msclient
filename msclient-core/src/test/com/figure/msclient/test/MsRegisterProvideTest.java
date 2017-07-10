package com.figure.msclient.test;

import com.figure.msclient.MsReferenceProvide;
import com.figure.msclient.metadata.ReferenceBean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuanbo.wei on 2017/3/25.
 */
@Component
public class MsRegisterProvideTest implements MsReferenceProvide {

    public final static String SERVICE_ID_USERCENTER = "usercenter";

    public final static String SERVICE_ID_ZID = "zidService";

    @Override
    public List<ReferenceBean> getReferences() {
        List<ReferenceBean> beans = new ArrayList<ReferenceBean>();
        beans.add(new ReferenceBean(SERVICE_ID_USERCENTER, "com.figure.user.dubbo.IBaseUserTokenService", "1.0",0));
        beans.add(new ReferenceBean(SERVICE_ID_ZID, "figure-service", 1));
        return beans;
    }
}
