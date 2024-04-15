package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.scut.xx.majorgraduation.common.redis.constant.RedisConstant;
import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.core.exception.ExceptionUtil;
import cn.scut.xx.majorgraduation.core.exception.ServiceException;
import cn.scut.xx.majorgraduation.dao.mapper.ModuleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.RoleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.RoleModuleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.UserRoleMapper;
import cn.scut.xx.majorgraduation.dao.po.ModulePO;
import cn.scut.xx.majorgraduation.dao.po.RoleModulePO;
import cn.scut.xx.majorgraduation.dao.po.RolePO;
import cn.scut.xx.majorgraduation.dao.po.UserRolePO;
import cn.scut.xx.majorgraduation.pojo.dto.req.*;
import cn.scut.xx.majorgraduation.pojo.dto.resp.RoleRespDTO;
import cn.scut.xx.majorgraduation.service.IRoleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 徐鑫
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RolePO> implements IRoleService {
    private final RoleMapper roleMapper;
    private final ModuleMapper moduleMapper;
    private final RoleModuleMapper roleModuleMapper;
    private final UserRoleMapper userRoleMapper;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void save(RoleSaveReqDTO roleSaveReqDTO) {
        RolePO role = BeanUtil.toBean(roleSaveReqDTO, RolePO.class);
        try {
            baseMapper.insert(role);
        } catch (Exception e) {
            String errorCode = ExceptionUtil.getErrorCode(e);
            if (errorCode.contains(ExceptionUtil.UNIQUE_ERROR)) {
                throw new ServiceException("角色名已存在，请另取角色名！");
            }
            throw e;
        }
    }

    @Override
    public void addModule(RoleModuleSaveReqDTO roleModuleSaveReqDTO) {
        checkRoleThrow(roleModuleSaveReqDTO.getRoleId());
        checkModuleThrow(roleModuleSaveReqDTO.getModuleId());
        RoleModulePO roleModule = BeanUtil.toBean(roleModuleSaveReqDTO, RoleModulePO.class);
        try {
            roleModuleMapper.insert(roleModule);
        } catch (DuplicateKeyException e) {
            throw new ClientException("该角色已授权该模块，请勿重复操作！");
        }
    }

    @Override
    public void removeModule(RoleModuleRemoveReqDTO roleModuleRemoveReqDTO) {
        LambdaQueryWrapper<RoleModulePO> query = new LambdaQueryWrapper<>();
        Long roleModuleId = roleModuleRemoveReqDTO.getRoleModuleId();
        if (roleModuleId != null && roleModuleId != 0) {
            query.eq(RoleModulePO::getId, roleModuleId);
        } else {
            Long roleId = roleModuleRemoveReqDTO.getRoleId();
            checkRoleThrow(roleId);
            Long moduleId = roleModuleRemoveReqDTO.getModuleId();
            checkModuleThrow(moduleId);

            query.eq(RoleModulePO::getRoleId, roleId)
                    .eq(RoleModulePO::getModuleId, moduleId);
        }
        int result = roleModuleMapper.delete(query);
        if (result < 1) {
            throw new ClientException("该角色并未授权该模块，无需删除！");
        }
    }

    @Override
    public List<RoleRespDTO> getAll() {
        LambdaQueryWrapper<RolePO> query = new LambdaQueryWrapper<RolePO>()
                .eq(RolePO::getStatus, 0);
        List<RolePO> roles = baseMapper.selectList(query);
        return BeanUtil.copyToList(roles, RoleRespDTO.class);
    }

    @Override
    public void batchAddModule(BatchAddRoleModuleReqDTO request) {

        LambdaQueryWrapper<RolePO> roleQuery = new LambdaQueryWrapper<RolePO>()
                .in(RolePO::getRoleId, request.getRoleIds())
                .select(RolePO::getRoleId);
        List<Long> roleIds = getIds(roleMapper.selectMaps(roleQuery));

        LambdaQueryWrapper<ModulePO> moduleQuery = new LambdaQueryWrapper<ModulePO>()
                .in(ModulePO::getModuleId, request.getModuleIds())
                .select(ModulePO::getModuleId);
        List<Long> moduleIds = getIds(moduleMapper.selectMaps(moduleQuery));

        List<RoleModulePO> toSaveData = new ArrayList<>();
        List<String> needToDelUserKeys = new ArrayList<>();
        for (Long roleId : roleIds) {
            stringRedisTemplate.delete(RedisConstant.CACHE_ROLE_MODULE + roleId);
            for (Long moduleId : moduleIds) {
                RoleModulePO po = new RoleModulePO();
                po.setModuleId(moduleId);
                po.setRoleId(roleId);
                toSaveData.add(po);
            }
            // 删除拥有该角色的用户缓存
            String roleUserKey = RedisConstant.CACHE_ROLE_USER + roleId;
            Set<String> userIds = stringRedisTemplate.opsForSet().members(roleUserKey);
            if (CollectionUtil.isEmpty(userIds)) {
                continue;
            }
            needToDelUserKeys.addAll(userIds.stream().map(userId->RedisConstant.CACHE_USER_MODULE + userId).toList());
        }
        // 删除拥有该角色的用户缓存
        stringRedisTemplate.delete(needToDelUserKeys);

        toSaveData.forEach(data -> {
            try {
                roleModuleMapper.insert(data);
            } catch (Exception e) {
                log.debug(e.toString());
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long roleId) {
        int i = baseMapper.deleteById(roleId);
        if (i <= 0) {
            throw new ClientException("数据异常，该角色本不存在！");
        }
        LambdaQueryWrapper<RoleModulePO> roleModuleQuery = new LambdaQueryWrapper<>();
        roleModuleQuery.eq(RoleModulePO::getRoleId, roleId);
        roleModuleMapper.delete(roleModuleQuery);
        LambdaQueryWrapper<UserRolePO> userRoleQuery = new LambdaQueryWrapper<>();
        userRoleQuery.eq(UserRolePO::getRoleId, roleId);
        userRoleMapper.delete(userRoleQuery);

        deleteRoleCache(roleId);
    }

    @Override
    public void update(RoleUpdateReqDTO roleUpdateReqDTO) {
        RolePO toUpdate = BeanUtil.toBean(roleUpdateReqDTO, RolePO.class);
        int i = baseMapper.updateById(toUpdate);
        if (i <= 0) {
            throw new ClientException("数据异常，该角色本不存在，无法修改！");
        }
        deleteRoleCache(roleUpdateReqDTO.getRoleId());
    }

    private void deleteRoleCache(Long roleId) {
        // 删缓存
        List<String> needToDelKeys = new ArrayList<>();
        // 角色信息缓存
        needToDelKeys.add(RedisConstant.CACHE_ROLE + roleId);
        // 角色所具有模块权限信息
        needToDelKeys.add(RedisConstant.CACHE_ROLE_MODULE + roleId);
        // 已登录用户的缓存信息
        needToDelKeys.add(RedisConstant.CACHE_ROLE_USER + roleId);
        String roleUserKey = RedisConstant.CACHE_ROLE_USER + roleId;
        Set<String> users = stringRedisTemplate.opsForSet().members(roleUserKey);
        if (!CollectionUtil.isEmpty(users)) {
            needToDelKeys.addAll(users);
        }
        stringRedisTemplate.delete(needToDelKeys);
    }

    private List<Long> getIds(List<Map<String, Object>> maps) {
        return maps.stream().map(map -> {
            Set<String> keySet = map.keySet();
            for (String key :
                    keySet) {
                return Long.parseLong(map.get(key).toString());
            }
            return 0L;
        }).toList();
    }

    private void checkRoleThrow(Long roleId) {
        RolePO role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new ServiceException("角色或模块不存在！");
        }
    }

    private void checkModuleThrow(Long moduleId) {
        ModulePO module = moduleMapper.selectById(moduleId);
        if (module == null) {
            throw new ServiceException("角色或模块不存在！");
        }
    }
}
