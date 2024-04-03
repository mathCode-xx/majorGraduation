package cn.scut.xx.majorgraduation.pojo.dto.req;

import cn.scut.xx.majorgraduation.common.web.PageReq;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author 徐鑫
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserSearchReqDTO extends PageReq {
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
     * 用户状态，见系统字典kind=1，0表示有效，1表示无效
     */
    private Integer status;
}
