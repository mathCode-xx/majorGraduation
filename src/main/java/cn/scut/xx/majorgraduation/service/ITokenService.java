package cn.scut.xx.majorgraduation.service;

import cn.scut.xx.majorgraduation.dao.po.UserPO;

/**
 * @author 徐鑫
 */
public interface ITokenService {

    /**
     * 从token中解析出user信息
     *
     * @param token token
     * @return user
     */
    UserPO getUserInfoFromToken(String token);

    /**
     * 将user信息转化为token
     *
     * @param user user
     * @return token
     */
    String generateTokenByUser(UserPO user);

    /**
     * 刷新token的持续时间
     *
     * @param token token
     */
    void flushToken(String token);
}
