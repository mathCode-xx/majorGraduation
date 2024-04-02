package cn.scut.xx.majorgraduation.dao.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 系统模块管理
 *
 * @author 徐鑫
 */
@TableName("t_module")
@Data
public class ModulePO {

    /**
     * 主键
     */
    @TableId("module_id")
    private Long moduleId;

    /**
     * 编号（对外）
     */
    @TableField("code")
    private Long code;

    /**
     * 模块名
     */
    @TableField("name")
    private String name;

    /**
     * 模块状态，请见系统字典kind=4
     */
    @TableField("status")
    private Integer status;

    /**
     * 模块所对应的url
     */
    @TableField("url")
    private String url;

    /**
     * 上层模块id
     */
    @TableField("upper_module_id")
    private Long upperModuleId;

    /**
     * 模块层级，用于前端显示
     */
    @TableField("module_level")
    private Integer moduleLevel;
}
