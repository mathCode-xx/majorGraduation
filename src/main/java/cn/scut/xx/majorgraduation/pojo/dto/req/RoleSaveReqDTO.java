package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class RoleSaveReqDTO {
    /**
     * 角色名
     */
    private String roleName;

    /**
     * 备注
     */
    private String remark;
}
