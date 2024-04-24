package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.scut.xx.majorgraduation.common.redis.utils.RedisUtils;
import cn.scut.xx.majorgraduation.common.utils.JwtUtil;
import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.dao.po.UserPO;
import cn.scut.xx.majorgraduation.service.ITokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static cn.scut.xx.majorgraduation.core.errorcode.BaseErrorCode.TOKEN_DELETED_ERROR;

/**
 * @author 徐鑫
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {
    /**
     * token中存储user info的key
     */
    private final String CLAIMS_USER_KEY = "user";
    /**
     * token过期时间为15分钟
     */
    private final long TOKEN_EXP_TIME = 1000 * 60 * 15;
    /**
     * flush token过期时间为15天
     */
    private final long FLUSH_TOKEN_EXP_TIME = 1000 * 60 * 60 * 24 * 15;
    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public UserPO getUserInfoFromToken(String token) {
        if (StrUtil.isEmpty(token)) {
            throw new ClientException(TOKEN_DELETED_ERROR);
        }
        return parseUserFromToken(token);
    }

    @Override
    public String generateTokenByUser(UserPO user) {
        String userJson = JSONUtil.toJsonStr(user);
        Map<String, Object> payload = new HashMap<>(1);
        payload.put(CLAIMS_USER_KEY, userJson);
        String token = JwtUtil.generateToken(TOKEN_EXP_TIME, payload);
        // 将token存入redis
        Duration duration = RedisUtils.getRandomTime();
        stringRedisTemplate.opsForValue().set(getRedisKeyByUserId(user.getUserId()), token, duration);
        return token;
    }

    @Override
    public String generateFlushTokenByUser(UserPO user) {
        Map<String, Object> payload = new HashMap<>(1);
        payload.put(CLAIMS_USER_KEY, user.getUserId());
        return JwtUtil.generateToken(FLUSH_TOKEN_EXP_TIME, payload);
    }

    @Override
    public Long getUserIdFromFlushToken(String flushToken) {
        if (StrUtil.isEmpty(flushToken)) {
            return null;
        }
        JWT jwt = JwtUtil.generateJwtFromToken(flushToken);
        if (!jwt.validate(0)) {
            // flush token已过期
            return null;
        }
        Object payload = jwt.getPayload(CLAIMS_USER_KEY);
        return Long.parseLong(payload.toString());
    }

    private String getRedisKeyByUserId(Long userId) {
        return RedisUtils.getCacheTokenKey(userId);
    }

    private UserPO parseUserFromToken(String token) {
        JWT jwt = JwtUtil.generateJwtFromToken(token);
        if (!jwt.validate(0)) {
            // token已过期
            throw new ClientException(TOKEN_DELETED_ERROR);
        }
        String userJson = jwt
                .getPayload(CLAIMS_USER_KEY)
                .toString();
        return JSONUtil.toBean(userJson, UserPO.class);
    }
}
