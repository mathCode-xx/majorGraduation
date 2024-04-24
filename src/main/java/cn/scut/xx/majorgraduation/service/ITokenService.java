package cn.scut.xx.majorgraduation.service;

import cn.scut.xx.majorgraduation.dao.po.UserPO;
import org.springframework.lang.Nullable;

/**
 * @author 徐鑫
 */
public interface ITokenService {

    /**
     * 从token中解析出user信息
     *
     * @param token token
     * @return user 若为空，表明token失效，需flush token
     */
    @Nullable
    UserPO getUserInfoFromToken(String token);

    /**
     * 将user信息转化为token
     *
     * @param user user
     * @return token
     */
    String generateTokenByUser(UserPO user);

    /**
     * 生成用于刷新token的token
     *
     * @param user 用户信息
     * @return flushToken
     */
    String generateFlushTokenByUser(UserPO user);

    /**
     * 从flush token中获取用户id
     *
     * @param flushToken flush token
     * @return 用户ID，若为空表明flush token失效，需重新登录
     */
    @Nullable
    Long getUserIdFromFlushToken(String flushToken);
}
