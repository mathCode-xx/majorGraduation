package cn.scut.xx.majorgraduation.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * @author 徐鑫
 */
@Component
public class SpringUtil implements ApplicationContextAware, EnvironmentAware {
    private static ApplicationContext applicationContext;
    private static Environment environment;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        SpringUtil.environment = environment;
    }

    public static <T> T getBean(Class<T> clz) {
        return applicationContext.getBean(clz);
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }
}
