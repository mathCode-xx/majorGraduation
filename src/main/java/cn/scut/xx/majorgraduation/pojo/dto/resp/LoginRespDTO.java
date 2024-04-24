package cn.scut.xx.majorgraduation.pojo.dto.resp;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class LoginRespDTO {
    /**
     * 存储用户信息的token
     */
    private String token;
    /**
     * 用于刷新token的token
     */
    private String flushToken;
}
