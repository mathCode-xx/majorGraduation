package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.dao.mapper.UserMapper;
import cn.scut.xx.majorgraduation.dao.po.UserPO;
import cn.scut.xx.majorgraduation.pojo.dto.req.UserSaveReqDTO;
import cn.scut.xx.majorgraduation.redis.constant.RedisConstant;
import cn.scut.xx.majorgraduation.service.IUserService;
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

    @Override
    public void save(UserSaveReqDTO userSaveReqDTO) {
        if(!checkUserNameIfNot(userSaveReqDTO.getUserName())) {
            throw new ClientException(USER_NAME_EXIST_ERROR);
        }
        RLock lock = redissonClient.getLock(RedisConstant.LOCK_USER_REGISTER);
        try {
            if(lock.tryLock()) {
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
}
