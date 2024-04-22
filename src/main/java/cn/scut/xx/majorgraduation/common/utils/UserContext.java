package cn.scut.xx.majorgraduation.common.utils;

import cn.scut.xx.majorgraduation.dao.po.UserPO;

/**
 * @author 徐鑫
 */
public class UserContext {
    private static final ThreadLocal<UserPO> userThreadLocal = new ThreadLocal<>();

    public static UserPO getUser() {
        return userThreadLocal.get();
    }

    public static void setUser(UserPO user) {
        userThreadLocal.set(user);
    }

    public static void clearUser() {
        userThreadLocal.remove();
    }
}
