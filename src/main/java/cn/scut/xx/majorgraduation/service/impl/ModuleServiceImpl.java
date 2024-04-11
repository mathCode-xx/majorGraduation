package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONUtil;
import cn.scut.xx.majorgraduation.common.redis.constant.RedisConstant;
import cn.scut.xx.majorgraduation.common.redis.utils.RedisUtils;
import cn.scut.xx.majorgraduation.core.exception.ServiceException;
import cn.scut.xx.majorgraduation.dao.mapper.ModuleMapper;
import cn.scut.xx.majorgraduation.dao.po.*;
import cn.scut.xx.majorgraduation.pojo.dto.req.ModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.ModuleRespDTO;
import cn.scut.xx.majorgraduation.service.IModuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 徐鑫
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, ModulePO> implements IModuleService {
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<ModuleRespDTO> getAll() {
        List<ModulePO> list = baseMapper.selectList(null);
        return BeanUtil.copyToList(list, ModuleRespDTO.class);
    }

    @Override
    public void save(ModuleSaveReqDTO moduleSaveReqDTO) {
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
                return range.stream().map(string -> JSONUtil.toBean(string, ModuleRespDTO.class)).toList();
            }
        }
        MPJLambdaWrapper<ModulePO> query = new MPJLambdaWrapper<>();
        query.selectAll(ModulePO.class)
                .leftJoin(RoleModulePO.class, RoleModulePO::getModuleId, ModulePO::getModuleId)
                .leftJoin(RolePO.class, RolePO::getRoleId, RoleModulePO::getRoleId)
                .leftJoin(UserRolePO.class, UserRolePO::getRoleId, RolePO::getRoleId)
                .leftJoin(UserPO.class, UserPO::getUserId, UserRolePO::getUserId)
                .eq(UserPO::getUserId, userId);
        List<ModuleRespDTO> result = baseMapper.selectJoinList(ModuleRespDTO.class, query);
        if (CollectionUtil.isEmpty(result)) {
            stringRedisTemplate.opsForValue().set(key, "");
            stringRedisTemplate.expire(key, RedisUtils.getRandomTime());
            return result;
        }
        stringRedisTemplate.opsForList().rightPushAll(key, result.stream().map(JSONUtil::toJsonStr).toList());
        stringRedisTemplate.expire(key, RedisUtils.getRandomTime());
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
