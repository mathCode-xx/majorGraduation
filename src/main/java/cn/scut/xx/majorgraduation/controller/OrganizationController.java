package cn.scut.xx.majorgraduation.controller;

import cn.scut.xx.majorgraduation.core.result.Result;
import cn.scut.xx.majorgraduation.core.result.Results;
import cn.scut.xx.majorgraduation.pojo.dto.req.OrganizationSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.OrganizationSearchReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.OrganizationUpdateReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.OrganizationRespDTO;
import cn.scut.xx.majorgraduation.service.IOrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 徐鑫
 */
@RestController
@RequestMapping("organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final IOrganizationService service;

    @GetMapping()
    public Result<List<OrganizationRespDTO>> get(OrganizationSearchReqDTO request) {
        List<OrganizationRespDTO> result = service.get(request);
        return Results.success(result);
    }

    @PostMapping()
    public Result<Void> save(@RequestBody OrganizationSaveReqDTO request) {
        service.save(request);
        return Results.success();
    }

    @PutMapping()
    public Result<Void> update(@RequestBody OrganizationUpdateReqDTO request) {
        service.update(request);
        return Results.success();
    }

    @DeleteMapping()
    public Result<Void> delete(@RequestParam("organizationId") Long organizationId) {
        service.delete(organizationId);
        return Results.success();
    }
}
