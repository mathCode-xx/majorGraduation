package cn.scut.xx.majorgraduation.controller;

import cn.scut.xx.majorgraduation.core.result.Result;
import cn.scut.xx.majorgraduation.core.result.Results;
import cn.scut.xx.majorgraduation.pojo.dto.req.RoleModuleRemoveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.RoleModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.RoleSaveReqDTO;
import cn.scut.xx.majorgraduation.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author 徐鑫
 */
@RestController
@RequestMapping("role")
@RequiredArgsConstructor
public class RoleController {
    private final IRoleService roleService;

    @PostMapping()
    public Result<Void> save(@RequestBody RoleSaveReqDTO request) {
        roleService.save(request);
        return Results.success();
    }

    @PostMapping("module")
    public Result<Void> addModule(@RequestBody RoleModuleSaveReqDTO request) {
        roleService.addModule(request);
        return Results.success();
    }

    @DeleteMapping("module")
    public Result<Void> removeModule(@RequestBody RoleModuleRemoveReqDTO request) {
        roleService.removeModule(request);
        return Results.success();
    }
}
