package com.figure.msclient.config.ms;

import com.figure.msclient.constant.ServiceRpcTypeConstant;
import com.figure.msclient.metadata.ReferenceBean;
import com.figure.msclient.util.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuanbo.wei on 2017/3/20.
 */
public class DefaultMsReferenceConfig implements MsReferenceConfig {
    private static final Logger logger = LoggerFactory.getLogger(DefaultMsReferenceConfig.class);

    private final String CONFIG_FILE_NAME = "/ms-config.xml";

    @Override
    public List<ReferenceBean> load() {
        InputStream fileStream = null;
        SAXReader reader = new SAXReader();
        List<ReferenceBean> beans = null;
        try {
            fileStream = this.getClass().getResourceAsStream(CONFIG_FILE_NAME);
            if(null==fileStream){
                logger.info("不使用ms-config.xml配置指定引用的服务");
                return null;
            }
            // 解析默认配置.
            beans = getConfigValue(reader.read(fileStream));
        } catch (DocumentException e) {
            logger.error("解析ms-config.xml文件出错");
        } finally {
            if(null!=fileStream){
                try {
                    fileStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return beans;
    }

    private List<ReferenceBean> getConfigValue(Document document){
        List<ReferenceBean> beans = null;
        if (document != null) {
            List<Node> references = document.selectNodes("/references/reference");
            if(references != null && references.size()>0){
                beans = new ArrayList<ReferenceBean>();
                String id = null;
                String serviceId = null;
                String version = null;
                String group = null;
                String protocol = null;
                String desc = null;
                ReferenceBean bean = null;
                for (Node reference : references) {//id="" serviceId="" version="" group="" protocol="" desc=""
                    bean = new ReferenceBean();
                    Element ele = (Element) reference;
                    id = ele.attribute("id").getValue();
                    serviceId = ele.attribute("serviceId").getValue();
                    version = ele.attribute("version").getValue();
                    group = ele.attribute("group").getValue();
                    protocol = ele.attribute("protocol").getValue();
                    desc = ele.attribute("desc").getValue();
                    bean.setId(id);
                    bean.setServiceId(serviceId);
                    if(StringUtils.isEmpty(version)){
                        bean.setVersion(null);
                    }else {
                        bean.setVersion(version);
                    }
                    if(StringUtils.isEmpty(group)){
                        bean.setGroup(null);
                    }else {
                        bean.setGroup(group);
                    }
                    bean.setDesc(desc);
                    if(StringUtils.isEmpty(protocol)){
                        logger.error("ms-config.xml配置错误，protocol不能为空");
                        throw new NullPointerException("ms-config.xml配置错误，protocol不能为空");
                    }
                    bean.setProtocolType(new Integer(protocol));

                    valid(bean);

                    beans.add(bean);
                }
            }
        }
        return beans;
    }

    private void valid(ReferenceBean bean){
        if(StringUtils.isEmpty(bean.getId())){
            logger.error("ms-config.xml配置错误，id不能为空");
            throw new NullPointerException("ms-config.xml配置错误，id不能为空");
        }

        if(StringUtils.isEmpty(bean.getServiceId())){
            logger.error("ms-config.xml配置错误，id不能为空");
            throw new NullPointerException("ms-config.xml配置错误，serviceId不能为空，" + bean.getId());
        }

        if(!ServiceRpcTypeConstant.isExists(bean.getProtocolType())){
            logger.error("ms-config.xml配置错误，protocol取值不对，【0:dubbo,1:springcloud】");
            throw new IllegalArgumentException("ms-config.xml配置错误，protocol取值不对，【0:dubbo,1:springcloud】" + bean.getId());
        }
    }
}
