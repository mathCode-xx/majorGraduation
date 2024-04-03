package cn.scut.xx.majorgraduation.redis.constant;

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

}
