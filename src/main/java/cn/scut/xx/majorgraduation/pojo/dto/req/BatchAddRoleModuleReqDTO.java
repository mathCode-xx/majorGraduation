package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

import java.util.List;

/**
 * @author 徐鑫
 */
@Data
public class BatchAddRoleModuleReqDTO {
    /**
     * 角色Id
     */
    List<Long> roleIds;
    /**
     * 模块Id
     */
    List<Long> moduleIds;
}
