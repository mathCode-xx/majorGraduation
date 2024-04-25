package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.scut.xx.majorgraduation.common.database.UserStatusEnum;
import cn.scut.xx.majorgraduation.common.redis.constant.RedisConstant;
import cn.scut.xx.majorgraduation.common.redis.utils.RedisUtils;
import cn.scut.xx.majorgraduation.common.utils.MD5Utils;
import cn.scut.xx.majorgraduation.common.utils.UserContext;
import cn.scut.xx.majorgraduation.common.utils.ValidateUtil;
import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.dao.mapper.RoleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.UserMapper;
import cn.scut.xx.majorgraduation.dao.mapper.UserRoleMapper;
import cn.scut.xx.majorgraduation.dao.po.RolePO;
import cn.scut.xx.majorgraduation.dao.po.UserPO;
import cn.scut.xx.majorgraduation.dao.po.UserRolePO;
import cn.scut.xx.majorgraduation.pojo.dto.req.*;
import cn.scut.xx.majorgraduation.pojo.dto.resp.LoginRespDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.UserRespDTO;
import cn.scut.xx.majorgraduation.service.ITokenService;
import cn.scut.xx.majorgraduation.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static cn.scut.xx.majorgraduation.core.errorcode.BaseErrorCode.*;

/**
 * @author 徐鑫
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserPO> implements IUserService {
    private final RedissonClient redissonClient;
    private final UserRoleMapper userRoleMapper;
    private final RoleMapper roleMapper;
    private final ITokenService tokenService;
    private final StringRedisTemplate stringRedisTemplate;
    private final RBloomFilter<String> phoneNumberCachePenetrationBloomFilter;
    private final RBloomFilter<String> idCardCachePenetrationBloomFilter;

    @Override
    public void save(UserSaveReqDTO userSaveReqDTO) {
        if (checkIdCardOrPhoneExist(userSaveReqDTO.getIdCard(), userSaveReqDTO.getPhoneNumber())) {
            throw new ClientException(ID_CARD_OR_PHONE_NUMBER_EXIST_ERROR);
        }
        RLock lock = redissonClient.getLock(RedisConstant.LOCK_USER_REGISTER);
        try {
            if (lock.tryLock()) {
                UserPO user = BeanUtil.toBean(userSaveReqDTO, UserPO.class);
                user.setUpdatePasswordTime(LocalDateTime.now());
                user.setPassword(MD5Utils.encrypt(user.getPassword()));
                try {
                    baseMapper.insert(user);
                } catch (DuplicateKeyException ex) {
                    throw new ClientException(ID_CARD_OR_PHONE_NUMBER_EXIST_ERROR);
                }
                phoneNumberCachePenetrationBloomFilter.add(user.getPhoneNumber());
                idCardCachePenetrationBloomFilter.add(user.getIdCard());
                return;
            }
            throw new ClientException(USER_NAME_EXIST_ERROR);
        } finally {
            lock.unlock();
        }
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

    @Override
    public Page<UserRespDTO> searchList(UserSearchReqDTO userSearchReqDTO) {
        LambdaQueryWrapper<UserPO> query = new LambdaQueryWrapper<>();
        fillQueryCondition(query, userSearchReqDTO);
        Page<UserPO> page = new Page<>(userSearchReqDTO.getCurrent(), userSearchReqDTO.getSize());
        baseMapper.selectPage(page, query);
        Page<UserRespDTO> result = new Page<>();
        BeanUtil.copyProperties(page, result);
        result.setRecords(BeanUtil.copyToList(page.getRecords(), UserRespDTO.class));
        return result;
    }

    @Override
    public LoginRespDTO login(UserLoginReqDTO userLoginReqDTO) {
        String md5 = MD5Utils.encrypt(userLoginReqDTO.getPassword());
        LambdaQueryWrapper<UserPO> query = new LambdaQueryWrapper<>();
        query.eq(UserPO::getPhoneNumber, userLoginReqDTO.getPhoneNumber())
                .eq(UserPO::getPassword, md5);
        UserPO user = baseMapper.selectOne(query);
        if (user == null) {
            throw new ClientException("用户名或密码错误");
        }
        String token = stringRedisTemplate.opsForValue()
                .get(RedisUtils.getCacheTokenKey(user.getUserId()));
        if (StrUtil.isEmpty(token)) {
            token = tokenService.generateTokenByUser(user);
        }
        String flushToken = tokenService.generateFlushTokenByUser(user);
        LoginRespDTO result = new LoginRespDTO();
        result.setToken(token);
        result.setFlushToken(flushToken);
        return result;
    }

    @Override
    public UserRespDTO getUserInfoFromToken(String token) {
        UserPO user = UserContext.getUser();
        return BeanUtil.toBean(user, UserRespDTO.class);
    }

    @Override
    public void updateUserInfo(UserUpdateReqDTO userUpdateReqDTO) {
        UserPO newUser = BeanUtil.toBean(userUpdateReqDTO, UserPO.class);
        if (!StrUtil.isEmpty(userUpdateReqDTO.getPassword())) {
            newUser.setPassword(MD5Utils.encrypt(userUpdateReqDTO.getPassword()));
        }
        int updateNum;
        try {
            updateNum = baseMapper.updateById(newUser);
        } catch (DuplicateKeyException e) {
            throw new ClientException("数据冲突，请检查手机号是否重复！");
        }
        if (updateNum <= 0) {
            throw new ClientException("数据出错，用户不存在！");
        }
    }

    @Override
    public void deleteUser(Long userId) {
        int deleteNum = baseMapper.deleteById(userId);
        if (deleteNum <= 0) {
            throw new ClientException("数据出错，该用户不存在！");
        }
    }

    @Override
    public void updateCurrent(UserUpdateCurrentInfoReqDTO userUpdateCurrentInfoReqDTO) {
        UserPO newUser = BeanUtil.toBean(userUpdateCurrentInfoReqDTO, UserPO.class);
        newUser.setUserId(UserContext.getUser().getUserId());
        int updateNum;
        try {
            updateNum = baseMapper.updateById(newUser);
        } catch (DuplicateKeyException e) {
            throw new ClientException("数据冲突，请检查手机号是否重复！");
        }
        if (updateNum <= 0) {
            throw new ClientException("数据出错，用户不存在！");
        }
    }

    @Override
    public String flushToken(String flushToken) {
        Long userId = tokenService.getUserIdFromFlushToken(flushToken);
        if (userId == null) {
            throw new ClientException(FLUSH_TOKEN_DELETED_ERROR);
        }
        UserPO user = baseMapper.selectById(userId);
        return tokenService.generateTokenByUser(user);
    }

    @Override
    public boolean checkPhoneExist(String phoneNumber) {
        return checkIdCardOrPhoneExist(null, phoneNumber);
    }

    @Override
    public boolean checkIdCardExist(String idCard) {
        return checkIdCardOrPhoneExist(idCard, null);
    }

    private boolean checkIdCardOrPhoneExist(@Nullable String idCard, @Nullable String phoneNumber) {
        LambdaQueryWrapper<UserPO> query = new LambdaQueryWrapper<>();
        if (StrUtil.isNotEmpty(idCard)) {
            if (!ValidateUtil.validateIdCard(idCard)) {
                throw new ClientException(ID_CARD_VERIFY_ERROR);
            }
            if (!idCardCachePenetrationBloomFilter.contains(idCard)) {
                return false;
            }
            query.eq(UserPO::getIdCard, idCard);
        }
        if (StrUtil.isNotEmpty(phoneNumber)) {
            if (!ValidateUtil.validatePhoneNumber(phoneNumber)) {
                throw new ClientException(PHONE_VERIFY_ERROR);
            }
            if (!phoneNumberCachePenetrationBloomFilter.contains(phoneNumber)) {
                return false;
            }
            query.eq(UserPO::getPhoneNumber, phoneNumber);
        }
        return baseMapper.selectOne(query) != null;
    }

    private void fillQueryCondition(LambdaQueryWrapper<UserPO> query, UserSearchReqDTO userSearchReqDTO) {
        if (userSearchReqDTO.getUserId() != null) {
            // 有id就是精确查询
            query.eq(UserPO::getUserId, userSearchReqDTO.getUserId());
            return;
        }

        if (userSearchReqDTO.getUserName() != null) {
            // 用户名模糊查询
            query.like(UserPO::getUserName, userSearchReqDTO.getUserName());
        }
        if (userSearchReqDTO.getPhoneNumber() != null) {
            // 手机号模糊查询
            query.like(UserPO::getPhoneNumber, userSearchReqDTO.getPhoneNumber());
        }
        if (userSearchReqDTO.getStatus() != null) {
            boolean sign = false;
            UserStatusEnum[] values = UserStatusEnum.values();
            for (UserStatusEnum value : values) {
                Integer status = value.getStatus();
                if (userSearchReqDTO.getStatus().equals(status)) {
                    query.eq(UserPO::getStatus, status);
                    sign = true;
                }
            }
            if (!sign) {
                throw new ClientException("参数出错，请确保用户状态值参数正确！");
            }
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
