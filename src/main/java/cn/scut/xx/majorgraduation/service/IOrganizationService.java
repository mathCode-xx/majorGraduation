package cn.scut.xx.majorgraduation.service;

import cn.scut.xx.majorgraduation.pojo.dto.req.OrganizationSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.OrganizationSearchReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.OrganizationUpdateReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.OrganizationRespDTO;

import java.util.List;

/**
 * @author 徐鑫
 */
public interface IOrganizationService {
    /**
     * 获取机构数据
     *
     * @param searchConditions 查询条件
     * @return 结果集
     */
    List<OrganizationRespDTO> get(OrganizationSearchReqDTO searchConditions);

    /**
     * 新增机构
     *
     * @param toSaveData 需要存储的参数
     */
    void save(OrganizationSaveReqDTO toSaveData);

    /**
     * 修改数据
     *
     * @param toUpdateData 需要修改的数据
     */
    void update(OrganizationUpdateReqDTO toUpdateData);

    /**
     * 删除机构数据
     *
     * @param organizationId 机构id
     */
    void delete(Long organizationId);
}
