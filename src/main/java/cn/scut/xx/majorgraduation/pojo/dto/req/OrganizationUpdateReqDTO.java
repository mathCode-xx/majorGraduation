package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class OrganizationUpdateReqDTO {
    /**
     * id
     */
    private Long id;
    /**
     * 机构名
     */
    private String name;
    /**
     * 机构编码
     */
    private String code;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 管理员id
     */
    private Long managerId;
    /**
     * 备注
     */
    private String remark;
}
