package cn.scut.xx.majorgraduation.common.redis.utils;

import cn.hutool.core.util.RandomUtil;

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
}
