package cn.scut.xx.majorgraduation.common.utils;

import cn.hutool.crypto.digest.MD5;

import java.nio.charset.StandardCharsets;

/**
 * @author 徐鑫
 */
public class MD5Utils {
    public static String encrypt(String str) {
        return MD5.create().digestHex16(str, StandardCharsets.UTF_8);
    }
}
