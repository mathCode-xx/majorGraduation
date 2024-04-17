package cn.scut.xx.majorgraduation.common.utils;

import java.util.regex.Pattern;

/**
 * @author 徐鑫
 */
public class ValidateUtil {
    public static boolean validatePhoneNumber(String phoneNumber) {
        return Pattern.matches("^1[3-9]\\d{9}$", phoneNumber);
    }
}
