package cn.scut.xx.majorgraduation.common.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.signers.JWTSignerUtil;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @author 徐鑫
 */
public class JwtUtil {
    private static final String SECRET = SpringUtil.getProperty("jwt.secret");
    private static String generateToken(Date expTime, Map<String, Object> payload) {
        JWT jwt = JWT.create();
        payload.forEach(jwt::setPayload);
        return jwt.setExpiresAt(expTime)
                .setKey(SECRET.getBytes(StandardCharsets.UTF_8))
                .setSigner(JWTSignerUtil.hs256(SECRET.getBytes(StandardCharsets.UTF_8)))
                .sign();
    }

    public static String generateToken(long durationTime, Map<String, Object> payload) {
        Date now = new Date();
        return generateToken(new Date(now.getTime() + durationTime), payload);
    }

    public static JWT generateJwtFromToken(String token) {
        return JWT.of(token).setKey(SECRET.getBytes(StandardCharsets.UTF_8));
    }
}
