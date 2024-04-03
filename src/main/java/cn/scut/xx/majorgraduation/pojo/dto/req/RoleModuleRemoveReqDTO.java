package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class RoleModuleRemoveReqDTO {
    /**
     * 角色-模块对应id，与下面两个二选一
     */
    private Long roleModuleId;

    /**
     * 角色id
     */
    private Long roleId;
    /**
     * 模块id
     */
    private Long moduleId;
}
