package cn.scut.xx.majorgraduation.dao.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统角色
 *
 * @author 徐鑫
 */
@Data
@TableName("t_role")
public class RolePO {

    /**
     * 主键
     */
    @TableId("role_id")
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
     * 角色状态，请见系统字典kind=3
     */
    @TableField("status")
    private Integer status;
}