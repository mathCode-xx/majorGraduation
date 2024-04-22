package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.dao.mapper.OrganizationMapper;
import cn.scut.xx.majorgraduation.dao.mapper.UserMapper;
import cn.scut.xx.majorgraduation.dao.po.OrganizationPO;
import cn.scut.xx.majorgraduation.dao.po.UserPO;
import cn.scut.xx.majorgraduation.pojo.dto.req.OrganizationSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.OrganizationSearchReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.OrganizationUpdateReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.OrganizationRespDTO;
import cn.scut.xx.majorgraduation.service.IOrganizationService;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.scut.xx.majorgraduation.core.errorcode.BaseErrorCode.ORGANIZATION_NOT_EXIST_ERROR;
import static cn.scut.xx.majorgraduation.core.errorcode.BaseErrorCode.ORGANIZATION_SAVE_NO_MANAGER_ERROR;

/**
 * @author 徐鑫
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, OrganizationPO> implements IOrganizationService {
    private final UserMapper userMapper;

    @Override
    public List<OrganizationRespDTO> get(OrganizationSearchReqDTO searchConditions) {
        MPJLambdaWrapper<OrganizationPO> mpj = new MPJLambdaWrapper<>();
        if (StrUtil.isNotEmpty(searchConditions.getName())) {
            mpj.like(OrganizationPO::getName, searchConditions.getName());
        }
        if (StrUtil.isNotEmpty(searchConditions.getCode())) {
            mpj.like(OrganizationPO::getCode, searchConditions.getCode());
        }
        mpj.selectAll(OrganizationPO.class)
                .selectAssociation(UserPO.class, OrganizationRespDTO::getManager)
                .leftJoin(UserPO.class, UserPO::getUserId, OrganizationPO::getManagerId);
        if (StrUtil.isNotEmpty(searchConditions.getManagerName())) {
            mpj.like(UserPO::getUserName, searchConditions.getManagerName());
        }
        return baseMapper.selectJoinList(OrganizationRespDTO.class, mpj);
    }

    @Override
    public void save(OrganizationSaveReqDTO toSaveData) {
        if (toSaveData.getManagerId() != null) {
            // 检查管理员id是否存在
            checkManagerExist(toSaveData.getManagerId());
        } else {
            // 管理员可以在新增机构时不指定，如果不指定，则先设置为负数，毕竟用户id不可能为负数
            toSaveData.setManagerId(-1L);
        }
        baseMapper.insert(BeanUtil.toBean(toSaveData, OrganizationPO.class));
    }

    @Override
    public void update(OrganizationUpdateReqDTO toUpdateData) {
        LambdaUpdateWrapper<OrganizationPO> query = new LambdaUpdateWrapper<>();
        query.eq(OrganizationPO::getId, toUpdateData.getId());
        boolean hasUpdate = false;
        if (StrUtil.isNotEmpty(toUpdateData.getName())) {
            hasUpdate = true;
            query.set(OrganizationPO::getName, toUpdateData.getName());
        }
        if (StrUtil.isNotEmpty(toUpdateData.getCode())) {
            hasUpdate = true;
            query.set(OrganizationPO::getCode, toUpdateData.getCode());
        }
        if (ObjectUtil.isNotEmpty(toUpdateData.getStatus())) {
            hasUpdate = true;
            query.set(OrganizationPO::getStatus, toUpdateData.getStatus());
        }
        if (StrUtil.isNotEmpty(toUpdateData.getRemark())) {
            hasUpdate = true;
            query.set(OrganizationPO::getRemark, toUpdateData.getRemark());
        }
        if (ObjectUtil.isNotEmpty(toUpdateData.getManagerId())) {
            hasUpdate = true;
            checkManagerExist(toUpdateData.getManagerId());
            query.set(OrganizationPO::getManagerId, toUpdateData.getManagerId());
        }
        int updateCount = baseMapper.update(query);
        if (hasUpdate && updateCount <= 0) {
            throw new ClientException(ORGANIZATION_NOT_EXIST_ERROR);
        }
    }

    @Override
    public void delete(Long organizationId) {
        int deleteCount = baseMapper.deleteById(organizationId);
        if (deleteCount <= 0) {
            throw new ClientException(ORGANIZATION_NOT_EXIST_ERROR);
        }
    }

    /**
     * 检查管理员是否存在
     *
     * @param managerId 管理员id
     * @throws ClientException 如果不存在，则抛出异常
     */
    private void checkManagerExist(Long managerId) {
        UserPO manager = userMapper.selectById(managerId);
        if (manager == null) {
            throw new ClientException(ORGANIZATION_SAVE_NO_MANAGER_ERROR);
        }
    }
}
