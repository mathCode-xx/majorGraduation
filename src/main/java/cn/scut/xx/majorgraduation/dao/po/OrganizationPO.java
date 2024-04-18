package cn.scut.xx.majorgraduation.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 机构
 *
 * @author 徐鑫
 */
@Data
@TableName("t_organization")
@EqualsAndHashCode(callSuper = true)
public class OrganizationPO extends BasePO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 上级机构id
     */
    @TableField(value = "upper_id")
    private Long upperId;
    /**
     * 机构名
     */
    @TableField(value = "name")
    private String name;
    /**
     * 机构编码
     */
    @TableField(value = "code")
    private String code;
    /**
     * 状态
     */
    @TableField(value = "status")
    private Integer status;
    /**
     * 管理员id，对应user表
     */
    @TableField(value = "manager_id")
    private Long managerId;
    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;
}
