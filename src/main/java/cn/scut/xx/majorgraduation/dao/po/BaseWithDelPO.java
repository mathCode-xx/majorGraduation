package cn.scut.xx.majorgraduation.dao.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 带逻辑删除的基类
 *
 * @author 徐鑫
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseWithDelPO extends BasePO{
    /**
     * 逻辑删除字段，请见系统字典kind=2
     */
    @TableField("delete_flag")
    @TableLogic(value = "0", delval = "1")
    private Integer deleteFlag;
}
