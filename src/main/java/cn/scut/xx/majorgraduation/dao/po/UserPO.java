package cn.scut.xx.majorgraduation.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 用户表
 *
 * @author 徐鑫
 */
@Data
@TableName("t_user")
@EqualsAndHashCode(callSuper = true)
public class UserPO extends BasePO {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 密码，经MD5加密
     */
    @TableField("password")
    private String password;

    /**
     * 手机号
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 最近登录时间
     */
    @TableField("login_time")
    private LocalDateTime loginTime;

    /**
     * 最近修改密码时间
     */
    @TableField("update_password_time")
    private LocalDateTime updatePasswordTime;

    /**
     * 用户状态，见系统字典kind=1，0表示有效，1表示无效
     */
    @TableField("status")
    private Integer status;
}