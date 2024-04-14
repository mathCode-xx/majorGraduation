package cn.scut.xx.majorgraduation.service;

import cn.scut.xx.majorgraduation.pojo.dto.req.BatchAddRoleModuleReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.RoleModuleRemoveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.RoleModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.RoleSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.RoleRespDTO;

import java.util.List;

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

    /**
     * 为角色授权模块访问权限
     *
     * @param roleModuleSaveReqDTO 请求参数
     */
    void addModule(RoleModuleSaveReqDTO roleModuleSaveReqDTO);

    /**
     * 移除角色对模块的授权
     *
     * @param roleModuleRemoveReqDTO 请求参数
     */
    void removeModule(RoleModuleRemoveReqDTO roleModuleRemoveReqDTO);

    /**
     * 获取所有角色信息
     *
     * @return 数据
     */
    List<RoleRespDTO> getAll();

    /**
     * 批量为角色授予模块权限
     *
     * @param request 请求参数
     * @return
     */
    void batchAddModule(BatchAddRoleModuleReqDTO request);
}
