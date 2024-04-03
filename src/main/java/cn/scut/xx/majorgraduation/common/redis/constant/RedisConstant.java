package cn.scut.xx.majorgraduation.common.redis.constant;

/**
 * redis常数，大多是key前缀
 *
 * @author 徐鑫
 */
public class RedisConstant {

    /**
     * 锁系列
     */
    private static final String LOCK_PRI = "lock:";
    public static final String LOCK_USER_REGISTER = LOCK_PRI + "user_register";

    /**
     * 缓存key系列
     */
    private static final String CACHE_PRI = "cache:";
    public static final String CACHE_USER_MODULE = CACHE_PRI + "user:";

    /**
     * 缓存大小
     */
    public static Integer MAX_SIZE = Integer.MAX_VALUE;

}
