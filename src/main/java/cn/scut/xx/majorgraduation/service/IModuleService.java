package cn.scut.xx.majorgraduation.service;

import cn.scut.xx.majorgraduation.pojo.dto.req.ModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.ModuleRespDTO;

import java.util.List;

/**
 * @author 徐鑫
 */
public interface IModuleService {

    /**
     * 获取所有模块信息
     *
     * @return 查询到的数据
     */
    List<ModuleRespDTO> getAll();

    /**
     * 新增模块
     *
     * @param moduleSaveReqDTO 请求参数
     */
    void save(ModuleSaveReqDTO moduleSaveReqDTO);
}
