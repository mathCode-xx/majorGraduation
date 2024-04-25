package cn.scut.xx.majorgraduation.common.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.dao.po.UserPO;

import java.nio.charset.StandardCharsets;

import static cn.scut.xx.majorgraduation.core.errorcode.BaseErrorCode.DECRYPT_ERROR;

/**
 * 一个对对象进行加密的工具
 *
 * @author 徐鑫
 */
public class EncryptUtil {
    private static final String SECRET = SpringUtil.getProperty("secret");
    private static final SymmetricCrypto ENCRYPT_METHOD = new SymmetricCrypto(SymmetricAlgorithm.AES, SECRET.getBytes(StandardCharsets.UTF_8));

    public static void encryptUser(UserPO user) {
        user.setPassword(encryptStr(user.getPassword()));
    }

    public static void decryptUser(UserPO user) {
        try {
            user.setPassword(decryptStr(user.getPassword()));
        } catch (Exception e) {
            throw new ClientException(DECRYPT_ERROR);
        }
    }

    public static String encryptStr(String str) {
        return ENCRYPT_METHOD.encryptHex(str);
    }

    public static String decryptStr(String str) {
        return ENCRYPT_METHOD.decryptStr(str, CharsetUtil.CHARSET_UTF_8);
    }
}
