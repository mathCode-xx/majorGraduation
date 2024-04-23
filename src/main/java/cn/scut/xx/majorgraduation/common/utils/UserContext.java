package cn.scut.xx.majorgraduation.common.utils;

import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.dao.po.UserPO;
import org.springframework.lang.NonNull;

import static cn.scut.xx.majorgraduation.core.errorcode.BaseErrorCode.TOKEN_DELETED_ERROR;

/**
 * @author 徐鑫
 */
public class UserContext {
    private static final ThreadLocal<UserPO> USER_THREAD_LOCAL = new ThreadLocal<>();

    @NonNull
    public static UserPO getUser() {
        UserPO user = USER_THREAD_LOCAL.get();
        if (user == null) {
            throw new ClientException(TOKEN_DELETED_ERROR);
        }
        return user;
    }

    public static void setUser(UserPO user) {
        USER_THREAD_LOCAL.set(user);
    }

    public static void clearUser() {
        USER_THREAD_LOCAL.remove();
    }
}
