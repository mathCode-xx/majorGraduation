package cn.scut.xx.majorgraduation.common.redis.utils;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.scut.xx.majorgraduation.common.redis.constant.RedisConstant;

import java.time.Duration;

/**
 * @author 徐鑫
 */
public class RedisUtils {
    /**
     * 获取一个随机秒数
     */
    public static Duration getRandomTime() {
        int time = RandomUtil.randomInt(1000);
        return Duration.ofSeconds(time);
    }

    public static String getCacheTokenKey(Long userId) {
        if (userId == null || userId == 0L) {
            return null;
        }
        return RedisConstant.TOKEN_INFO + userId;
    }

    public static String getCacheUserInfoByTokenKey(String tokenKey) {
        if (StrUtil.isEmpty(tokenKey)) {
            return null;
        }
        if(tokenKey.startsWith(RedisConstant.TOKEN_USER_INFO)) {
            return tokenKey;
        }
        return RedisConstant.TOKEN_USER_INFO + tokenKey;
    }
}
