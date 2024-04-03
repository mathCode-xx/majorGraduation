package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.scut.xx.majorgraduation.core.exception.ServiceException;
import cn.scut.xx.majorgraduation.dao.mapper.ModuleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.RoleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.RoleModuleMapper;
import cn.scut.xx.majorgraduation.dao.po.ModulePO;
import cn.scut.xx.majorgraduation.dao.po.RoleModulePO;
import cn.scut.xx.majorgraduation.dao.po.RolePO;
import cn.scut.xx.majorgraduation.pojo.dto.req.RoleModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.service.RoleModuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author 徐鑫
 */
@Service
@RequiredArgsConstructor
public class RoleModuleServiceImpl extends ServiceImpl<RoleModuleMapper, RoleModulePO> implements RoleModuleService {

    private final RoleMapper roleMapper;
    private final ModuleMapper moduleMapper;
    @Override
    public void save(RoleModuleSaveReqDTO roleModuleSaveReqDTO) {
        RolePO role = roleMapper.selectById(roleModuleSaveReqDTO.getRoleId());
        ModulePO module = moduleMapper.selectById(roleModuleSaveReqDTO.getModuleId());
        if(role == null || module == null) {
            throw new ServiceException("角色或模块不存在！");
        }
        RoleModulePO roleModule = BeanUtil.toBean(roleModuleSaveReqDTO, RoleModulePO.class);
        baseMapper.insert(roleModule);
    }
}
