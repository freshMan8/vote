package com.tencent.wxcloudrun.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public SpringContextUtil() {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 根据bean名称获取实体
     *
     * @param name 名称
     * @return 实体
     * @throws BeansException 异常
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 根据类型获取bean
     *
     * @param clazz 类型
     * @return bean实体
     * @throws BeansException 异常
     */
    public static Object getBean(Class<?> clazz) throws BeansException {
        return applicationContext.getBean(clazz);
    }
}
