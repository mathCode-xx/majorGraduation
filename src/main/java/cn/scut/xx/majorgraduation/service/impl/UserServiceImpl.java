package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.dao.mapper.RoleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.UserMapper;
import cn.scut.xx.majorgraduation.dao.mapper.UserRoleMapper;
import cn.scut.xx.majorgraduation.dao.po.RolePO;
import cn.scut.xx.majorgraduation.dao.po.UserPO;
import cn.scut.xx.majorgraduation.dao.po.UserRolePO;
import cn.scut.xx.majorgraduation.pojo.dto.req.UserRoleAddReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.UserRoleRemoveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.UserSaveReqDTO;
import cn.scut.xx.majorgraduation.redis.constant.RedisConstant;
import cn.scut.xx.majorgraduation.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static cn.scut.xx.majorgraduation.core.errorcode.BaseErrorCode.USER_NAME_EXIST_ERROR;

/**
 * @author 徐鑫
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements IUserService {
    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;

    @Override
    public void save(UserSaveReqDTO userSaveReqDTO) {
        if (!checkUserNameIfNot(userSaveReqDTO.getUserName())) {
            throw new ClientException(USER_NAME_EXIST_ERROR);
        }
        RLock lock = redissonClient.getLock(RedisConstant.LOCK_USER_REGISTER);
        try {
            if (lock.tryLock()) {
                UserPO user = BeanUtil.toBean(userSaveReqDTO, UserPO.class);
                user.setUpdatePasswordTime(LocalDateTime.now());
                try {
                    baseMapper.insert(user);
                } catch (DuplicateKeyException ex) {
                    throw new ClientException(USER_NAME_EXIST_ERROR);
                }
                userRegisterCachePenetrationBloomFilter.add(user.getUserName());
                return;
            }
            throw new ClientException(USER_NAME_EXIST_ERROR);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean checkUserNameIfNot(String userName) {
        return !userRegisterCachePenetrationBloomFilter.contains(userName);
    }

    @Override
    public void addRole(UserRoleAddReqDTO userRoleAddReqDTO) {
        checkUserThrow(userRoleAddReqDTO.getUserId());
        checkRoleThrow(userRoleAddReqDTO.getRoleId());
        UserRolePO userRole = BeanUtil.toBean(userRoleAddReqDTO, UserRolePO.class);
        try {
            userRoleMapper.insert(userRole);
        } catch (DuplicateKeyException ex) {
            throw new ClientException("用户已拥有该角色，请勿重复添加");
        }
    }

    @Override
    public void removeRole(UserRoleRemoveReqDTO userRoleRemoveReqDTO) {
        LambdaQueryWrapper<UserRolePO> query = new LambdaQueryWrapper<>();
        Long userRoleId = userRoleRemoveReqDTO.getUserRoleId();
        // 删除条件二选一，要么传入用户-角色对应id，要么传入用户id和角色id
        if (userRoleId != null && userRoleId != 0) {
            query.eq(UserRolePO::getId, userRoleId);
        } else {
            checkUserThrow(userRoleRemoveReqDTO.getUserId());
            checkRoleThrow(userRoleRemoveReqDTO.getRoleId());
            query.eq(UserRolePO::getUserId, userRoleRemoveReqDTO.getUserId())
                    .eq(UserRolePO::getRoleId, userRoleRemoveReqDTO.getRoleId());
        }
        int result = userRoleMapper.delete(query);
        if (result < 1) {
            throw new ClientException("该用户不属于该角色！");
        }
    }

    private void checkUserThrow(Long userId) {
        UserPO user = baseMapper.selectById(userId);
        if (user == null) {
            throw new ClientException("用户不存在！");
        }
    }

    private void checkRoleThrow(Long roleId) {
        RolePO role = roleMapper.selectById(roleId);
        if (role == null) {
            throw new ClientException("角色不存在！");
        }
    }
}
