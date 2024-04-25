package cn.scut.xx.majorgraduation.common.utils;

import java.util.regex.Pattern;

/**
 * @author 徐鑫
 */
public class ValidateUtil {
    public static boolean validatePhoneNumber(String phoneNumber) {
        return Pattern.matches("^1[3-9]\\d{9}$", phoneNumber);
    }

    public static boolean validateIdCard(String idCard) {
        return Pattern.matches("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[\\dXx]$", idCard);
    }
}
