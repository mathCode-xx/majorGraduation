package cn.scut.xx.majorgraduation.pojo.dto.resp;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 徐鑫
 */
@Data
public class UserRespDTO {
    /**
     * 主键
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 微信
     */
    private String wechatId;

    /**
     * 最近登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 用户状态，见系统字典kind=1，0表示有效，1表示无效
     */
    private Integer status;
}
