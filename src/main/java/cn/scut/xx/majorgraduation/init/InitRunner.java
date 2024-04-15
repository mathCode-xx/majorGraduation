package cn.scut.xx.majorgraduation.init;

import cn.hutool.json.JSONUtil;
import cn.scut.xx.majorgraduation.common.redis.constant.RedisConstant;
import cn.scut.xx.majorgraduation.dao.mapper.ModuleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.RoleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.RoleModuleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.UserRoleMapper;
import cn.scut.xx.majorgraduation.dao.po.ModulePO;
import cn.scut.xx.majorgraduation.dao.po.RoleModulePO;
import cn.scut.xx.majorgraduation.dao.po.RolePO;
import cn.scut.xx.majorgraduation.dao.po.UserRolePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 应用初始化
 *
 * @author 徐鑫
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class InitRunner implements ApplicationRunner {
    private final StringRedisTemplate stringRedisTemplate;
    private final ModuleMapper moduleMapper;
    private final RoleMapper roleMapper;
    private final RoleModuleMapper roleModuleMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public void run(ApplicationArguments args) {
        initSysModuleCache();
        initSysRoleCache();
        initSysRoleModuleCache();
        initSysUserRoleCache();
    }

    private void initSysModuleCache() {
        List<ModulePO> modules = moduleMapper.selectList(null);
        log.debug(modules.toString());
        Map<String, String> cache = new HashMap<>();
        modules.forEach(module -> {
            String key = RedisConstant.CACHE_MODULE + module.getModuleId();
            cache.put(key, JSONUtil.toJsonStr(module));
        });
        stringRedisTemplate.delete(RedisConstant.CACHE_MODULE);
        stringRedisTemplate.opsForValue().multiSet(cache);
    }

    private void initSysRoleCache() {
        List<RolePO> roles = roleMapper.selectList(null);
        log.debug(roles.toString());
        Map<String, String> cache = new HashMap<>();
        roles.forEach(role -> {
            String key = RedisConstant.CACHE_ROLE + role.getRoleId();
            cache.put(key, JSONUtil.toJsonStr(role));
        });
        stringRedisTemplate.delete(RedisConstant.CACHE_ROLE);
        stringRedisTemplate.opsForValue().multiSet(cache);
    }

    private void initSysRoleModuleCache() {
        List<RoleModulePO> roleModules = roleModuleMapper.selectList(null);
        log.debug(roleModules.toString());
        Map<Long, List<String>> cache = new HashMap<>();
        roleModules.forEach(roleModule -> {
            List<String> value = cache.computeIfAbsent(roleModule.getRoleId(), k -> new ArrayList<>());
            value.add(roleModule.getModuleId().toString());
        });
        redisCacheList(cache, RedisConstant.CACHE_ROLE_MODULE);
    }

    private void initSysUserRoleCache() {
        List<UserRolePO> userRoles = userRoleMapper.selectList(null);
        Map<Long, Set<String>> cache = new HashMap<>();
        userRoles.forEach(userRole -> {
            Set<String> value = cache.computeIfAbsent(userRole.getRoleId(), k -> new HashSet<>());
            value.add(userRole.getUserId().toString());
        });
        redisCacheSet(cache, RedisConstant.CACHE_ROLE_USER);
    }

    private void redisCacheList(Map<Long, List<String>> data, String priKey) {
        data.forEach((mapKey, mapValue) -> {
            String key = priKey + mapKey;
            stringRedisTemplate.delete(key);
            stringRedisTemplate.opsForList().rightPushAll(key, mapValue);
        });
    }

    private void redisCacheSet(Map<Long, Set<String>> data, String priKey) {
        data.forEach((mapKey, mapValue) -> {
            String key = priKey + mapKey;
            stringRedisTemplate.delete(key);
            stringRedisTemplate.opsForSet().add(key, mapValue.toArray(new String[0]));
        });
    }
}
