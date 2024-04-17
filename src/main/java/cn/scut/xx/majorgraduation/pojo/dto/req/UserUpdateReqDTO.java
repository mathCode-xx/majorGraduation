package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class UserUpdateReqDTO {
    /**
     * 主键
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码，经MD5加密
     */
    private String password;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 用户状态，见系统字典kind=1，0表示有效，1表示无效
     */
    private Integer status;
}
