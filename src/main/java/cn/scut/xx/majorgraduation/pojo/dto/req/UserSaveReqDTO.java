package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class UserSaveReqDTO {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 个人邮箱
     */
    private String email;

    /**
     * 微信号
     */
    private String wechatId;
}
