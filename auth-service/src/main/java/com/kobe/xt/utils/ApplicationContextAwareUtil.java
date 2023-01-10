package com.kobe.xt.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author kobe_xt
 * @version 1.0
 * @description: 通过这个工具类获取无法通过正常方式注入的bean对象；
 * @date 2022/9/21 14:14
 */
@Slf4j
@Component
public class ApplicationContextAwareUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        ApplicationContextAwareUtil.applicationContext = context;
    }

    /**
     * @description: 获取 ApplicationContext
     * @author kobe_xt
     * @date: 2022/9/21 14:23
     * @param:
     * @return: org.springframework.context.ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @description: 通过 name 获取 bean
     * @author kobe_xt
     * @date: 2022/9/21 14:23
     * @param: name
     * @return: java.lang.Object
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * @description: 通过 Class 对象获取 bean
     * @author kobe_xt
     * @date: 2022/9/21 14:24
     * @param: clazz
     * @return: T
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }



    /**
     * @description: 通过 name、Clazz 获取 Bean
     * @author kobe_xt
     * @date: 2022/9/21 14:24
     * @param: name,clazz
     * @return: T
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
