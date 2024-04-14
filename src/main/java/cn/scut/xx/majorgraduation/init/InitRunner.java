package cn.scut.xx.majorgraduation.init;

import cn.hutool.json.JSONUtil;
import cn.scut.xx.majorgraduation.common.redis.constant.RedisConstant;
import cn.scut.xx.majorgraduation.dao.mapper.ModuleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.RoleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.RoleModuleMapper;
import cn.scut.xx.majorgraduation.dao.po.ModulePO;
import cn.scut.xx.majorgraduation.dao.po.RoleModulePO;
import cn.scut.xx.majorgraduation.dao.po.RolePO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void run(ApplicationArguments args) {
        initSysModuleCache();
        initSysRoleCache();
        initSysRoleModuleCache();
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
        cache.forEach((roleId, moduleIds) -> {
            String key = RedisConstant.CACHE_ROLE_MODULE + roleId;
            stringRedisTemplate.delete(key);
            stringRedisTemplate.opsForList().rightPushAll(key, moduleIds);
        });
    }
}
