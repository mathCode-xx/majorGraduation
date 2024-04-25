package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.scut.xx.majorgraduation.common.database.RoleStatusEnum;
import cn.scut.xx.majorgraduation.common.redis.constant.RedisConstant;
import cn.scut.xx.majorgraduation.common.redis.utils.RedisUtils;
import cn.scut.xx.majorgraduation.common.utils.UserUtil;
import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.core.exception.ServiceException;
import cn.scut.xx.majorgraduation.dao.mapper.ModuleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.RoleModuleMapper;
import cn.scut.xx.majorgraduation.dao.po.*;
import cn.scut.xx.majorgraduation.pojo.dto.req.ModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.ModuleSearchReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.ModuleRespDTO;
import cn.scut.xx.majorgraduation.service.IModuleService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.scut.xx.majorgraduation.core.errorcode.BaseErrorCode.AUTH_LIMIT;

/**
 * @author 徐鑫
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, ModulePO> implements IModuleService {
    private final StringRedisTemplate stringRedisTemplate;
    private final RoleModuleMapper roleModuleMapper;

    @Override
    public List<ModuleRespDTO> get(ModuleSearchReqDTO request) {
        LambdaQueryWrapper<ModulePO> query = new LambdaQueryWrapper<>();
        if (request.getUpperModuleId() != null) {
            query.eq(ModulePO::getUpperModuleId, request.getUpperModuleId());
        }
        List<ModulePO> list = baseMapper.selectList(query);
        return BeanUtil.copyToList(list, ModuleRespDTO.class);
    }

    @Override
    public void save(ModuleSaveReqDTO moduleSaveReqDTO) {
        if (UserUtil.loginIsSuperMan()) {
            throw new ClientException(AUTH_LIMIT);
        }
        ModulePO toInsert = BeanUtil.toBean(moduleSaveReqDTO, ModulePO.class);
        if (moduleSaveReqDTO.getUpperModuleId() != null
                && moduleSaveReqDTO.getUpperModuleId() != 0) {
            ModulePO upperModule = this.selectById(moduleSaveReqDTO.getUpperModuleId());
            toInsert.setModuleLevel(upperModule.getModuleLevel() + 1);
        }
        baseMapper.insert(toInsert);
    }

    @Override
    public List<ModuleRespDTO> getModuleByUser(Long userId) {
        String key = RedisConstant.CACHE_USER_MODULE + userId;
        Boolean hasKey = stringRedisTemplate.hasKey(key);
        if (hasKey != null && hasKey) {
            List<String> range = stringRedisTemplate.opsForList().range(key, 0, RedisConstant.MAX_SIZE);
            if (!CollectionUtil.isEmpty(range)) {
                List<ModulePO> modules = batchSelectById(range.stream().map(Long::parseLong).toList());
                return BeanUtil.copyToList(modules, ModuleRespDTO.class);
            }
        }
        MPJLambdaWrapper<ModulePO> query = new MPJLambdaWrapper<>();
        query.selectAll(ModulePO.class)
                .leftJoin(RoleModulePO.class, RoleModulePO::getModuleId, ModulePO::getModuleId)
                .leftJoin(RolePO.class, RolePO::getRoleId, RoleModulePO::getRoleId)
                .leftJoin(UserRolePO.class, UserRolePO::getRoleId, RolePO::getRoleId)
                .leftJoin(UserPO.class, UserPO::getUserId, UserRolePO::getUserId)
                .eq(UserPO::getUserId, userId)
                .eq(RolePO::getStatus, RoleStatusEnum.VALID);
        List<ModuleRespDTO> result = baseMapper.selectJoinList(ModuleRespDTO.class, query);
        if (CollectionUtil.isEmpty(result)) {
            return result;
        }
        List<String> cacheValues = result.stream()
                .map(module -> module.getModuleId().toString()).toList();
        stringRedisTemplate.opsForList().rightPushAll(key, cacheValues);
        stringRedisTemplate.expire(key, RedisUtils.getRandomTime());
        return result;

    }

    @Override
    public List<ModuleRespDTO> getByRole(Long roleId) {
        String key = RedisConstant.CACHE_ROLE_MODULE + roleId;
        List<String> moduleIdsStr = stringRedisTemplate.opsForList().range(key, 0, RedisConstant.MAX_SIZE);

        // 优先从缓存中获取
        if (!CollectionUtil.isEmpty(moduleIdsStr)) {
            List<Long> moduleIds = moduleIdsStr.stream()
                    .map(Long::parseLong)
                    .toList();
            List<ModulePO> modules = batchSelectById(moduleIds);
            return BeanUtil.copyToList(modules, ModuleRespDTO.class);
        }

        MPJLambdaWrapper<ModulePO> query = new MPJLambdaWrapper<>();
        query.selectAll(ModulePO.class)
                .leftJoin(RoleModulePO.class, RoleModulePO::getModuleId, ModulePO::getModuleId)
                .leftJoin(RolePO.class, RolePO::getRoleId, RoleModulePO::getRoleId)
                .eq(RolePO::getRoleId, roleId);
        List<ModulePO> modules = baseMapper.selectJoinList(ModulePO.class, query);
        List<ModuleRespDTO> result = BeanUtil.copyToList(modules, ModuleRespDTO.class);
        // todo 是否要缓存module？

        // 缓存role-module
        List<String> moduleIds = modules.stream()
                .map(d -> d.getModuleId().toString())
                .collect(Collectors.toList());
        stringRedisTemplate.delete(key);
        stringRedisTemplate.opsForList().rightPushAll(key, moduleIds);
        return result;
    }

    @Override
    public void delete(List<Long> moduleIds) {
        if (UserUtil.loginIsSuperMan()) {
            throw new ClientException(AUTH_LIMIT);
        }
        // 直接删除缓存和数据库
        moduleIds.forEach(moduleId -> {
            String key = RedisConstant.CACHE_MODULE + moduleId;
            stringRedisTemplate.delete(key);
        });
        LambdaQueryWrapper<ModulePO> moduleQuery = new LambdaQueryWrapper<ModulePO>()
                .in(ModulePO::getModuleId, moduleIds);
        baseMapper.delete(moduleQuery);

        // 删除role-module对应，这里只需要删除数据库，无需对缓存进行管理（也不好管）
        LambdaQueryWrapper<RoleModulePO> roleModuleQuery = new LambdaQueryWrapper<RoleModulePO>()
                .in(RoleModulePO::getModuleId, moduleIds);
        roleModuleMapper.delete(roleModuleQuery);
    }

    private List<ModulePO> batchSelectById(List<Long> ids) {
        List<ModulePO> result = new ArrayList<>();

        // 先从缓存中获取
        List<String> keys = ids.stream().map(id -> RedisConstant.CACHE_MODULE + id).toList();
        List<String> modulesStr = stringRedisTemplate.opsForValue().multiGet(keys);
        if (!CollectionUtil.isEmpty(modulesStr)) {
            result.addAll(modulesStr.stream().map(module -> JSONUtil.toBean(module, ModulePO.class)).toList());
        }

        // 剩下的从数据库中获取
        Set<Long> dataFromCache = result.stream().map(ModulePO::getModuleId).collect(Collectors.toSet());
        List<Long> needToSql = ids.stream().filter(id -> !dataFromCache.contains(id)).toList();
        if (!CollectionUtil.isEmpty(needToSql)) {
            result.addAll(baseMapper.selectBatchIds(needToSql));
        }
        return result;
    }

    public ModulePO selectById(Long id) {
        ModulePO upperModule = baseMapper.selectById(id);
        if (upperModule == null) {
            throw new ServiceException("id为" + id + "的模块不存在");
        }
        return upperModule;
    }
}
