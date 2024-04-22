package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * 机构查询条件
 *
 * @author 徐鑫
 */
@Data
public class OrganizationSearchReqDTO {
    /**
     * 机构名
     */
    private String name;
    /**
     * 机构编号
     */
    private String code;
    /**
     * 管理员名
     */
    private String managerName;
}
