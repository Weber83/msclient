package com.figure.msclient.config.loader;

import com.figure.msclient.config.ConfigLoader;
import com.figure.msclient.config.MSConfig;
import com.figure.msclient.config.dubbo.DubboProperties;
import com.figure.msclient.config.springcloud.EurekaClientProperties;
import com.figure.msclient.constant.MSClientModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chuanbo.wei on 2017/3/13.
 */
public abstract class AbstractConfigLoader implements ConfigLoader {
    private static final Logger logger = LoggerFactory.getLogger(AbstractConfigLoader.class);

    protected MSClientModel msClientModel;

    protected String appName;

    @Override
    public MSConfig load() {
        MSConfig msConfig = new MSConfig();
        if(MSClientModel.FULL.equals(msClientModel)){
            loadDubboProperties(msConfig);
            loadEurekaProperties(msConfig);
        } else if(MSClientModel.DUBBO.equals(msClientModel)){
            loadDubboProperties(msConfig);
        } else if(MSClientModel.SPRINGCLOUD.equals(msClientModel)){
            loadEurekaProperties(msConfig);
        }
        loadCommonProperties(msConfig);
        logger.info("MSClient ConfigLoader load ok.");
        return msConfig;
    }

    public abstract void loadCommonProperties(MSConfig msConfig);

    public abstract void loadEurekaProperties(MSConfig msConfig);

    public abstract void loadDubboProperties(MSConfig msConfig);
}
