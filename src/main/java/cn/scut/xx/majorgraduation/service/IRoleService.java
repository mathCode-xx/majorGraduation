package cn.scut.xx.majorgraduation.service;

import cn.scut.xx.majorgraduation.pojo.dto.req.RoleSaveReqDTO;

/**
 * @author 徐鑫
 */
public interface IRoleService {

    /**
     * 新增角色信息
     *
     * @param roleSaveReqDTO 角色信息
     */
    void save(RoleSaveReqDTO roleSaveReqDTO);
}
