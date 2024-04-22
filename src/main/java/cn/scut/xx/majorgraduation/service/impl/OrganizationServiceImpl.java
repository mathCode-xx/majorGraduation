package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static cn.scut.xx.majorgraduation.core.errorcode.BaseErrorCode.*;

/**
 * @author 徐鑫
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, OrganizationPO> implements IOrganizationService {
    private final UserMapper userMapper;

    @Override
    public List<OrganizationRespDTO> get(OrganizationSearchReqDTO searchConditions) {
        boolean useSearch = false;
        MPJLambdaWrapper<OrganizationPO> mpj = new MPJLambdaWrapper<>();
        mpj.selectAll(OrganizationPO.class)
                .selectAssociation(UserPO.class, OrganizationRespDTO::getManager)
                .leftJoin(UserPO.class, UserPO::getUserId, OrganizationPO::getManagerId);
        if (StrUtil.isNotEmpty(searchConditions.getName())) {
            useSearch = true;
            mpj.like(OrganizationPO::getName, searchConditions.getName());
        }
        if (StrUtil.isNotEmpty(searchConditions.getCode())) {
            useSearch = true;
            mpj.like(OrganizationPO::getCode, searchConditions.getCode());
        }
        if (StrUtil.isNotEmpty(searchConditions.getManagerName())) {
            useSearch = true;
            mpj.like(UserPO::getUserName, searchConditions.getManagerName());
        }
        List<OrganizationRespDTO> result = baseMapper.selectJoinList(OrganizationRespDTO.class, mpj);
        if (useSearch && CollectionUtil.isNotEmpty(result)) {
            // 如果使用了模糊查询，需要把查询到的机构拼接上其子机构
            List<Long> ids = result.stream().map(OrganizationRespDTO::getId).toList();
            List<Long> parentIds = result.stream().map(OrganizationRespDTO::getUpperId).toList();
            MPJLambdaWrapper<OrganizationPO> subMpj = new MPJLambdaWrapper<>();
            subMpj.selectAll(OrganizationPO.class)
                    .selectAssociation(UserPO.class, OrganizationRespDTO::getManager)
                    .leftJoin(UserPO.class, UserPO::getUserId, OrganizationPO::getManagerId);
            subMpj.in(OrganizationPO::getUpperId, ids);
            subMpj.or().in(OrganizationPO::getId, parentIds);
            List<OrganizationRespDTO> subResult = baseMapper.selectJoinList(OrganizationRespDTO.class, subMpj);
            result.addAll(subResult);
        }
        // 去重
        result = result.stream().collect(
                Collectors.collectingAndThen(
                        Collectors.toCollection(() ->
                                new TreeSet<>(Comparator.comparing(OrganizationRespDTO::getId))),
                        ArrayList::new));

        return result;
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
        try {
            baseMapper.insert(BeanUtil.toBean(toSaveData, OrganizationPO.class));
        } catch (DuplicateKeyException e) {
            throw new ClientException(ORGANIZATION_CODE_EXIST_ERROR);
        }
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
