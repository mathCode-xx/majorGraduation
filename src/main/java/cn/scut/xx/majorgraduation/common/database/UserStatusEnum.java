package cn.scut.xx.majorgraduation.common.database;

/**
 * @author 徐鑫
 */
public enum UserStatusEnum {
    /**
     * 用户有效
     */
    VALID(1),
    /**
     * 用户无效
     */
    NO_VALID(2);


    UserStatusEnum(Integer status) {
        this.status = status;
    }
    private final Integer status;
    public Integer getStatus() {
        return this.status;
    }
}
