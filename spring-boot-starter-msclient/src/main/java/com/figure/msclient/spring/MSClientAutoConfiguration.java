package com.figure.msclient.spring;

import com.figure.msclient.MSClientService;
import com.figure.msclient.MsReferenceProvide;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by chuanbo.wei on 2017/8/01.
 */
@Configuration
@ConditionalOnClass({MSClientService.class})
@EnableConfigurationProperties(MSClientProperties.class)
public class MSClientAutoConfiguration implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Autowired
    private MSClientProperties properties;

    @Bean
    @ConditionalOnMissingBean(MSClientService.class)//当容器中没有指定Bean的情况下
    public MSClientService msClientService(){
        MsReferenceProvide referenceProvide = getBean(MsReferenceProvide.class);
        MSClientService bean = new MSClientService(properties.getAppName(), properties.getLoader(), properties.getModel(), referenceProvide);
        bean.init();
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MSClientAutoConfiguration.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }
}
