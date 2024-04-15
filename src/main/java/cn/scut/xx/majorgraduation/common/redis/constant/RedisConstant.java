package cn.scut.xx.majorgraduation.common.redis.constant;

/**
 * redis常数，大多是key前缀
 *
 * @author 徐鑫
 */
public class RedisConstant {
    private static final String PROJECTION_NAME = "majorGraduation:";

    /**
     * 锁系列
     */
    private static final String LOCK_PRI = PROJECTION_NAME + "lock:";
    public static final String LOCK_USER_REGISTER = LOCK_PRI + "user_register";

    /**
     * 缓存key系列
     */
    private static final String CACHE_PRI = PROJECTION_NAME + "cache:";
    public static final String CACHE_USER_MODULE = CACHE_PRI + "user:module:";
    public static final String CACHE_MODULE = CACHE_PRI + "module:";
    public static final String CACHE_ROLE = CACHE_PRI + "role:";
    public static final String CACHE_ROLE_MODULE = CACHE_ROLE + "module:";

    /**
     * 缓存大小
     */
    public static Integer MAX_SIZE = Integer.MAX_VALUE;

    /**
     * token
     */
    private final static String TOKEN_PRI = PROJECTION_NAME + "token:";
    public final static String TOKEN_USER_INFO = TOKEN_PRI + "user:";
    public final static String TOKEN_INFO = TOKEN_PRI;
}
