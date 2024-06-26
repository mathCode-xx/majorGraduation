package cn.scut.xx.majorgraduation.controller;

import cn.scut.xx.majorgraduation.core.result.Result;
import cn.scut.xx.majorgraduation.core.result.Results;
import cn.scut.xx.majorgraduation.dao.po.UserPO;
import cn.scut.xx.majorgraduation.pojo.dto.req.ModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.ModuleSearchReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.ModuleRespDTO;
import cn.scut.xx.majorgraduation.service.IModuleService;
import cn.scut.xx.majorgraduation.service.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 徐鑫
 */
@RestController
@RequestMapping("module")
@RequiredArgsConstructor
public class ModuleController {

    private final IModuleService moduleService;
    private final ITokenService tokenService;

    @GetMapping("")
    public Result<List<ModuleRespDTO>> get(ModuleSearchReqDTO request) {
        return Results.success(moduleService.get(request));
    }

    @GetMapping("role")
    public Result<List<ModuleRespDTO>> getByRole(@RequestParam("roleId") Long roleId) {
        List<ModuleRespDTO> data = moduleService.getByRole(roleId);
        return Results.success(data);
    }

    @PostMapping()
    public Result<Void> save(@RequestBody ModuleSaveReqDTO moduleSaveReqDTO) {
        moduleService.save(moduleSaveReqDTO);
        return Results.success();
    }

    @GetMapping("user")
    public Result<List<ModuleRespDTO>> getModuleByUser(@RequestParam("userId") Long userId) {
        return Results.success(moduleService.getModuleByUser(userId));
    }

    @GetMapping("user/current")
    public Result<List<ModuleRespDTO>> getModuleFromCurrentUser(
            @RequestHeader(value = "token", required = false) String token) {
        UserPO user = tokenService.getUserInfoFromToken(token);
        return Results.success(moduleService.getModuleByUser(user.getUserId()));
    }

    @DeleteMapping("batch")
    public Result<Void> delete(@RequestParam("moduleIds") List<Long> request) {
        moduleService.delete(request);
        return Results.success();
    }
}
