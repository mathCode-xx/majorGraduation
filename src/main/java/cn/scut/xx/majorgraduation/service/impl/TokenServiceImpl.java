package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.scut.xx.majorgraduation.common.redis.utils.RedisUtils;
import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.dao.po.UserPO;
import cn.scut.xx.majorgraduation.service.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static cn.scut.xx.majorgraduation.core.errorcode.BaseErrorCode.TOKEN_DELETED_ERROR;

/**
 * @author 徐鑫
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {
    private final String CLAIMS_KEY = "token";
    private final StringRedisTemplate stringRedisTemplate;
    @Value("${jwt.secret}")
    private String secret;

    @Override
    public UserPO getUserInfoFromToken(String token) {
        if (StrUtil.isEmpty(token)) {
            throw new ClientException(TOKEN_DELETED_ERROR);
        }
        String tokenKey = parseRedisKeyFromToken(token);
        String userInfoStr = stringRedisTemplate.opsForValue().get(tokenKey);
        flushTokenKey(tokenKey);
        return JSONUtil.toBean(userInfoStr, UserPO.class);
    }

    @Override
    public String generateTokenByUser(UserPO user) {
        String tokenKey = getRedisKeyByTokenKey(UUID.randomUUID().toString());
        String token = JWT.create()
                .setPayload(CLAIMS_KEY, tokenKey)
                .setKey(secret.getBytes(StandardCharsets.UTF_8)).sign();

        // 将用户信息与token存入redis
        Duration duration = RedisUtils.getRandomTime();
        stringRedisTemplate.opsForValue().set(tokenKey, JSONUtil.toJsonStr(user), duration);
        stringRedisTemplate.opsForValue().set(getRedisKeyByUserId(user.getUserId()), token, duration);
        return token;
    }

    @Override
    public void flushToken(String token) {
        String tokenKey = parseRedisKeyFromToken(token);
        flushTokenKey(tokenKey);
    }

    private void flushTokenKey(String tokenKey) {
        flushTokenKey(tokenKey, RedisUtils.getRandomTime());
    }

    private void flushTokenKey(String tokenKey, Duration timeout) {
        tokenKey = getRedisKeyByTokenKey(tokenKey);
        String userJson = stringRedisTemplate.opsForValue().get(tokenKey);
        if (StrUtil.isEmpty(userJson)) {
            throw new ClientException(TOKEN_DELETED_ERROR);
        }
        UserPO user = JSONUtil.toBean(userJson, UserPO.class);
        stringRedisTemplate.expire(tokenKey, timeout);
        stringRedisTemplate.expire(getRedisKeyByUserId(user.getUserId()), timeout);
    }

    private String getRedisKeyByUserId(Long userId) {
        return RedisUtils.getCacheTokenKey(userId);
    }

    private String getRedisKeyByTokenKey(String tokenKey) {
        return RedisUtils.getCacheUserInfoByTokenKey(tokenKey);
    }

    private String parseRedisKeyFromToken(String token) {
        return JWT.of(token)
                .setKey(secret.getBytes(StandardCharsets.UTF_8))
                .getPayload(CLAIMS_KEY)
                .toString();
    }
}
