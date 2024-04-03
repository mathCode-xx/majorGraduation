package cn.scut.xx.majorgraduation.controller;

import cn.scut.xx.majorgraduation.core.result.Result;
import cn.scut.xx.majorgraduation.core.result.Results;
import cn.scut.xx.majorgraduation.pojo.dto.req.RoleModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.service.RoleModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 徐鑫
 */
@RestController
@RequestMapping("role-module")
@RequiredArgsConstructor
public class RoleModuleController {
    private final RoleModuleService roleModuleService;

    @PostMapping
    public Result<Void> save(@RequestBody RoleModuleSaveReqDTO request) {
        roleModuleService.save(request);
        return Results.success();
    }
}
