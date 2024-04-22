package cn.scut.xx.majorgraduation.core.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.scut.xx.majorgraduation.common.utils.SpringUtil;
import cn.scut.xx.majorgraduation.common.utils.UserContext;
import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.dao.po.UserPO;
import cn.scut.xx.majorgraduation.service.ITokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import static cn.scut.xx.majorgraduation.core.errorcode.BaseErrorCode.TOKEN_DELETED_ERROR;

/**
 * 全局拦截器
 *
 * @author 徐鑫
 */
@Slf4j
public class GlobalInterceptor implements HandlerInterceptor {

    private final ITokenService tokenService = SpringUtil.getBean(ITokenService.class);

    @Override
    public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        StringBuffer url = request.getRequestURL();
        log.info("访问接口：" + url.toString());
        // 检测是否登录
        String token = request.getHeader("token");
        if (StrUtil.isNotEmpty(token)) {
            UserPO user = tokenService.getUserInfoFromToken(token);
            log.info("登录用户：" + user.getUserName());
            UserContext.setUser(user);
            return true;
        } else {
            throw new ClientException(TOKEN_DELETED_ERROR);
        }
    }
}
