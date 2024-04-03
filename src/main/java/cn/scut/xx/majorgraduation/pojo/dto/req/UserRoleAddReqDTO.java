package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class UserRoleAddReqDTO {
    /**
     * 用户
     */
    private Long userId;

    /**
     * 角色
     */
    private Long roleId;
}
