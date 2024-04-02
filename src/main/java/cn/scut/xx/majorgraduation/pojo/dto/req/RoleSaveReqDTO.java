package cn.scut.xx.majorgraduation.pojo.dto.req;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class RoleSaveReqDTO {
    /**
     * 角色名
     */
    @TableField("role_name")
    private String roleName;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;
}
