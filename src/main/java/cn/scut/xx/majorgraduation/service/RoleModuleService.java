package cn.scut.xx.majorgraduation.service;

import cn.scut.xx.majorgraduation.pojo.dto.req.RoleModuleSaveReqDTO;

/**
 * @author 徐鑫
 */
public interface RoleModuleService {
    /**
     * 新增角色模块对应
     *
     * @param roleModuleSaveReqDTO 请求参数
     */
    void save(RoleModuleSaveReqDTO roleModuleSaveReqDTO);
}
