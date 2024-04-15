package cn.scut.xx.majorgraduation.service;

import cn.scut.xx.majorgraduation.pojo.dto.req.ModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.ModuleSearchReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.ModuleRespDTO;

import java.util.List;

/**
 * @author 徐鑫
 */
public interface IModuleService {

    /**
     * 获取所有模块信息
     *
     * @param request 查询参数
     * @return 查询到的数据
     */
    List<ModuleRespDTO> get(ModuleSearchReqDTO request);

    /**
     * 新增模块
     *
     * @param moduleSaveReqDTO 请求参数
     */
    void save(ModuleSaveReqDTO moduleSaveReqDTO);

    /**
     * 获取用户所授权的模块
     *
     * @param userId 用户id
     * @return 查询结果
     */
    List<ModuleRespDTO> getModuleByUser(Long userId);

    /**
     * 获取某角色所拥有的模块权限
     *
     * @param roleId 角色id
     * @return 数据
     */
    List<ModuleRespDTO> getByRole(Long roleId);

    /**
     * 删除模块信息
     *
     * @param moduleIds 模块id
     */
    void delete(List<Long> moduleIds);
}
