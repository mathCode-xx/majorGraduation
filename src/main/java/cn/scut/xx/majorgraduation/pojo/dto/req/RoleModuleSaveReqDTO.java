package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class RoleModuleSaveReqDTO {
    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 模块id
     */
    private Long moduleId;
}
