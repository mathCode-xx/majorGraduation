package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWT;
import cn.scut.xx.majorgraduation.common.redis.constant.RedisConstant;
import cn.scut.xx.majorgraduation.common.redis.utils.RedisUtils;
import cn.scut.xx.majorgraduation.dao.po.UserPO;
import cn.scut.xx.majorgraduation.service.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * @author 徐鑫
 */
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {
    private final String REDIS_SAVE_TOKEN_KEY = RedisConstant.TOKEN_USER_INFO;
    private final String CLAIMS_KEY = "token";
    private final StringRedisTemplate stringRedisTemplate;
    @Value("${jwt.secret}")
    private String secret;

    @Override
    public UserPO getUserInfoFromToken(String token) {
        String tokenKey = parseRedisKeyFromToken(token);
        String userInfoStr = stringRedisTemplate.opsForValue().get(tokenKey);
        return JSONUtil.toBean(userInfoStr, UserPO.class);
    }

    @Override
    public String generateTokenByUser(UserPO user) {
        String tokenKey = REDIS_SAVE_TOKEN_KEY + UUID.fastUUID();
        String token = JWT.create()
                .setPayload(CLAIMS_KEY, tokenKey)
                .setKey(secret.getBytes(StandardCharsets.UTF_8)).sign();
        flushTokenKey(tokenKey);
        return token;
    }

    @Override
    public void flushToken(String token) {
        String tokenKey = parseRedisKeyFromToken(token);
        flushTokenKey(tokenKey);
    }

    private void flushTokenKey(String tokenKey) {
        if (tokenKey.startsWith(REDIS_SAVE_TOKEN_KEY)) {
            stringRedisTemplate.expire(tokenKey, RedisUtils.getRandomTime());
        } else {
            stringRedisTemplate.expire(REDIS_SAVE_TOKEN_KEY + tokenKey, RedisUtils.getRandomTime());
        }
    }

    private String parseRedisKeyFromToken(String token) {
        return JWT.of(token)
                .setKey(secret.getBytes(StandardCharsets.UTF_8))
                .getPayload(CLAIMS_KEY)
                .toString();
    }
}
