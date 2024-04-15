package cn.scut.xx.majorgraduation.common.database;

/**
 * @author 徐鑫
 */
public enum RoleStatusEnum {
    /**
     * 用户有效
     */
    VALID(0),
    /**
     * 用户无效
     */
    NO_VALID(1);


    RoleStatusEnum(Integer status) {
        this.status = status;
    }
    private final Integer status;
    public Integer getStatus() {
        return this.status;
    }
}
