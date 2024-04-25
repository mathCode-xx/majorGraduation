package cn.scut.xx.majorgraduation.common.utils;

import cn.scut.xx.majorgraduation.dao.po.UserPO;

/**
 * @author 徐鑫
 */
public class UserUtil {
    public static boolean loginIsSuperMan() {
        UserPO user = UserContext.getUser();
        return isSuperMan(user);
    }

    public static boolean isSuperMan(UserPO user) {
        return user != null && user.getUserId() != null && user.getUserId() <= 0;
    }
}
