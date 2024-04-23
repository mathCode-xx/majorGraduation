package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * 可以修改的个人信息有：手机号、邮箱、微信号
 *
 * @author 徐鑫
 */
@Data
public class UserUpdateCurrentInfoReqDTO {
    /**
     * 手机号
     */
    private String phoneNumber;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 微信号
     */
    private String wechatId;
}
