package cn.scut.xx.majorgraduation.controller;

import cn.scut.xx.majorgraduation.core.result.Result;
import cn.scut.xx.majorgraduation.core.result.Results;
import cn.scut.xx.majorgraduation.pojo.dto.req.*;
import cn.scut.xx.majorgraduation.pojo.dto.resp.UserRespDTO;
import cn.scut.xx.majorgraduation.service.IUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author 徐鑫
 */
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("current")
    public Result<UserRespDTO> getCurrentUserInfo(@RequestHeader("token") String token) {
        return Results.success(userService.getUserInfoFromToken(token));
    }

    @PostMapping("login")
    public Result<String> login(@RequestBody UserLoginReqDTO request) {
        return Results.success(userService.login(request));
    }

    @PostMapping
    public Result<Void> save(@RequestBody UserSaveReqDTO request) {
        userService.save(request);
        return Results.success();
    }

    @GetMapping("user-name")
    public Result<Boolean> checkUserName(@RequestParam("user_name") String userName) {
        return Results.success(userService.checkUserNameIfNot(userName));
    }

    @PostMapping("role")
    public Result<Void> addRoleToUser(@RequestBody UserRoleAddReqDTO request) {
        userService.addRole(request);
        return Results.success();
    }

    @DeleteMapping("role")
    public Result<Void> removeRole(@RequestBody UserRoleRemoveReqDTO request) {
        userService.removeRole(request);
        return Results.success();
    }

    @GetMapping
    public Result<Page<UserRespDTO>> getAll(UserSearchReqDTO request) {
        return Results.success(userService.searchList(request));
    }

}
