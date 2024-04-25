package cn.scut.xx.majorgraduation.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 系统角色
 *
 * @author 徐鑫
 */
@Data
@TableName("t_role")
@EqualsAndHashCode(callSuper = true)
public class RolePO extends BasePO {

    /**
     * 主键
     */
    @TableId(value = "role_id", type = IdType.AUTO)
    private Long roleId;

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

    /**
     * 所属机构id，-1表明为所有机构共有
     */
    @TableField("organization_id")
    private Long organizationId;

    /**
     * 角色状态，请见系统字典kind=3
     */
    @TableField("status")
    private Integer status;
}