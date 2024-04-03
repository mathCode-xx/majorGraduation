package cn.scut.xx.majorgraduation.web.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 全局拦截器
 *
 * @author 徐鑫
 */
@Slf4j
public class GlobalInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        StringBuffer url = request.getRequestURL();
        log.info("访问接口：" + url.toString());
        return true;
    }
}
