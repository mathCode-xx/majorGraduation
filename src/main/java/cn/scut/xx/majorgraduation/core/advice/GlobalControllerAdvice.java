package cn.scut.xx.majorgraduation.core.advice;

import cn.scut.xx.majorgraduation.core.exception.AbstractException;
import cn.scut.xx.majorgraduation.core.exception.ServiceException;
import cn.scut.xx.majorgraduation.core.result.Result;
import cn.scut.xx.majorgraduation.core.result.Results;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * 全局 控制器 切面
 *
 * @author 徐鑫
 */
@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

//    /**
//     * 处理 权限认证时的异常
//     *
//     * @param e 异常
//     * @return 响应数据
//     */
//    @ExceptionHandler(value = AuthorizationException.class)
//    public Result<Void> handleException(AuthorizationException e) {
//        //获取错误中中括号的内容
//        e.printStackTrace();
//        String message = e.getMessage();
//        String msg = message.substring(message.indexOf("[") + 1, message.indexOf("]"));
//        //判断是角色错误还是权限错误
//        if (message.contains("role")) {
//            return Results.failure(new ClientException("对不起，您没有" + msg + "角色"));
//        } else if (message.contains("permission")) {
//            return Results.failure(new ClientException("对不起，您没有" + msg + "权限"));
//        } else {
//            return Results.failure(new ClientException("对不起，您的权限有误"));
//        }
//    }

    /**
     * 处理 自定义 业务异常
     *
     * @param e 业务异常
     * @return 响应数据
     */
    @ExceptionHandler(value = AbstractException.class)
    public Result<Void> handleException(AbstractException e) {
        e.printStackTrace();
        return Results.failure(e);
    }

    /**
     * 处理 其余异常
     *
     * @param e 异常
     * @return 响应数据
     */
    @ExceptionHandler(value = Throwable.class)
    public Result<Void> handleException(Throwable e) {
        e.printStackTrace();
        return Results.failure(new ServiceException(e.getMessage()));
    }
}

