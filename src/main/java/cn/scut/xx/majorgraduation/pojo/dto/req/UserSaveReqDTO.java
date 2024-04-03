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
}
