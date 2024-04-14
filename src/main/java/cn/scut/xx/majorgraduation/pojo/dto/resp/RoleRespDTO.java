package cn.scut.xx.majorgraduation.pojo.dto.resp;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class RoleRespDTO {

    /**
     * 主键
     */
    private Long roleId;

    /**
     * 角色名
     */
    private String roleName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 角色状态，请见系统字典kind=3
     */
    private Integer status;
}
