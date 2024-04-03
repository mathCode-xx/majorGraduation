package cn.scut.xx.majorgraduation.dao.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 徐鑫
 */
@Data
public class BasePO {
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /**
     * 逻辑删除字段，请见系统字典kind=2
     */
    @TableField("delete_flag")
    @TableLogic(value = "0", delval = "1")
    private Integer deleteFlag;
}
