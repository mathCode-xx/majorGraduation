package cn.scut.xx.majorgraduation.controller;

import cn.scut.xx.majorgraduation.core.result.Result;
import cn.scut.xx.majorgraduation.core.result.Results;
import cn.scut.xx.majorgraduation.pojo.dto.req.ModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.ModuleRespDTO;
import cn.scut.xx.majorgraduation.service.IModuleService;
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

    @GetMapping("all")
    public Result<List<ModuleRespDTO>> getAll() {
        return Results.success(moduleService.getAll());
    }

    @PostMapping()
    public Result<Void> save(@RequestBody ModuleSaveReqDTO moduleSaveReqDTO) {
        moduleService.save(moduleSaveReqDTO);
        return Results.success();
    }
}
