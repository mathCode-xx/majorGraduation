package cn.scut.xx.majorgraduation.core.exception;

import cn.hutool.core.util.StrUtil;

/**
 * @author 徐鑫
 */
public class ExceptionUtil {
    /**
     * 主键/唯一键冲突的异常信息
     */
    public static final String UNIQUE_ERROR = "Duplicate entry";

    /**
     * 获取到错误描述
     */
    public static String getErrorCode(Exception e) {
        String errorCode = "";
        if (e.getCause() != null && e.getCause().getMessage() != null) {
            errorCode = e.getCause().getMessage();
        }
        if (StrUtil.isEmpty(errorCode) && e.getMessage() != null) {
            errorCode = e.getMessage();
        }
        return errorCode;
    }
}
