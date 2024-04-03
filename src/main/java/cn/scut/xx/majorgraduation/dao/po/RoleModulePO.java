package cn.scut.xx.majorgraduation.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色-模块 对应表，角色拥有哪些模块的权限
 *
 * @author 徐鑫
 */
@Data
@TableName("t_role_module")
public class RoleModulePO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 角色id
     */
    @TableField("role_id")
    private Long roleId;

    /**
     * 模块id
     */
    @TableField("module_id")
    private Long moduleId;

    /**
     * 删除标记，请见系统字典kind=2
     */
    @TableField("delete_flag")
    private Integer deleteFlag;

}