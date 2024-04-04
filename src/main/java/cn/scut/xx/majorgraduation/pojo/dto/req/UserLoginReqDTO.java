package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class UserLoginReqDTO {
    private String phoneNumber;
    private String password;
}
