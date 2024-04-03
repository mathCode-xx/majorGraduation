package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class UserRoleRemoveReqDTO {
    /**
     * 用户-角色id，此参数与下两个参数二选一
     */
    private Long userRoleId;

    /**
     * 用户
     */
    private Long userId;
    /**
     * 角色
     */
    private Long roleId;
}
