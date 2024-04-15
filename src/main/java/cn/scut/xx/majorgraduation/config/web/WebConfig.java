package cn.scut.xx.majorgraduation.config.web;

import cn.scut.xx.majorgraduation.core.interceptor.GlobalInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 徐鑫
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalInterceptor());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许的源
                .allowedOrigins("http://localhost:8080")
                // 允许的HTTP方法
                .allowedMethods("*")
                // 允许的请求头
                .allowedHeaders("*")
                // 允许暴露的响应头
                .exposedHeaders("*")
                // 是否允许发送Cookie
                .allowCredentials(true)
                // 预检请求的有效期，单位为秒
                .maxAge(3600);
    }
}
