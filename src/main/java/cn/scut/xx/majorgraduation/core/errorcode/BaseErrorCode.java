package cn.scut.xx.majorgraduation.core.errorcode;

/**
 * 基础错误码定义
 *
 * @author 徐鑫
 */
public enum BaseErrorCode implements IErrorCode {

    // ========== 一级宏观错误码 客户端错误 ==========
    CLIENT_ERROR("A000001", "用户端错误"),

    // ========== 二级宏观错误码 用户注册错误 ==========
    USER_REGISTER_ERROR("A000100", "用户注册错误"),
    USER_NAME_VERIFY_ERROR("A000110", "用户名校验失败"),
    USER_NAME_EXIST_ERROR("A000111", "用户名已存在"),
    PHONE_NUMBER_EXIST_ERROR("A000112", "手机号已存在"),
    PHONE_VERIFY_ERROR("A000113", "手机格式不正确"),

    // ========== 二级宏观错误码 系统请求缺少幂等Token ==========
    IDEMPOTENT_TOKEN_NULL_ERROR("A000200", "幂等Token为空"),
    IDEMPOTENT_TOKEN_DELETE_ERROR("A000201", "幂等Token已被使用或失效"),
    FLUSH_TOKEN_DELETED_ERROR("A000202", "登录过期，请重新登录"),
    TOKEN_DELETED_ERROR("A000203", "token已失效，请重新登录"),

    // ========== 二级宏观错误码 机构管理系统 ==========
    ORGANIZATION_ERROR("A000300", "机构管理系统异常"),
    ORGANIZATION_NOT_EXIST_ERROR("A000301", "数据异常！该机构不存在"),
    ORGANIZATION_SAVE_NO_MANAGER_ERROR("A000302", "管理员id不存在"),
    ORGANIZATION_CODE_EXIST_ERROR("A000303", "机构代码已存在"),

    // ========== 二级宏观错误码 权限管理模块 ==========
    ROLE_NAME_EXIST("A000401", "角色名已存在"),
    AUTH_LIMIT("A000402", "权限不足"),

    // ========== 一级宏观错误码 系统执行出错 ==========
    SERVICE_ERROR("B000001", "系统执行出错"),
    // ========== 二级宏观错误码 系统执行超时 ==========
    SERVICE_TIMEOUT_ERROR("B000100", "系统执行超时"),
    DECRYPT_ERROR("B000101", "系统执行出错"),

    // ========== 一级宏观错误码 调用第三方服务出错 ==========
    REMOTE_ERROR("C000001", "调用第三方服务出错");

    private final String code;

    private final String message;

    BaseErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}
