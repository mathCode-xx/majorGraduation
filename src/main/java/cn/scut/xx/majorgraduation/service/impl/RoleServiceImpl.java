package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.scut.xx.majorgraduation.core.exception.ClientException;
import cn.scut.xx.majorgraduation.core.exception.ExceptionUtil;
import cn.scut.xx.majorgraduation.core.exception.ServiceException;
import cn.scut.xx.majorgraduation.dao.mapper.ModuleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.RoleMapper;
import cn.scut.xx.majorgraduation.dao.mapper.RoleModuleMapper;
import cn.scut.xx.majorgraduation.dao.po.ModulePO;
import cn.scut.xx.majorgraduation.dao.po.RoleModulePO;
import cn.scut.xx.majorgraduation.dao.po.RolePO;
import cn.scut.xx.majorgraduation.pojo.dto.req.RoleModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.req.RoleSaveReqDTO;
import cn.scut.xx.majorgraduation.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

/**
 * @author 徐鑫
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RolePO> implements IRoleService {
    private final RoleMapper roleMapper;
    private final ModuleMapper moduleMapper;
    private final RoleModuleMapper roleModuleMapper;
    @Override
    public void save(RoleSaveReqDTO roleSaveReqDTO) {
        RolePO role = BeanUtil.toBean(roleSaveReqDTO, RolePO.class);
        try {
            baseMapper.insert(role);
        } catch (Exception e) {
            String errorCode = ExceptionUtil.getErrorCode(e);
            if (errorCode.contains(ExceptionUtil.UNIQUE_ERROR)) {
                throw new ServiceException("角色名已存在，请另取角色名！");
            }
            throw e;
        }
    }

    @Override
    public void addModule(RoleModuleSaveReqDTO roleModuleSaveReqDTO) {
        checkRoleThrow(roleModuleSaveReqDTO.getRoleId());
        checkModuleThrow(roleModuleSaveReqDTO.getModuleId());
        RoleModulePO roleModule = BeanUtil.toBean(roleModuleSaveReqDTO, RoleModulePO.class);
        try {
            roleModuleMapper.insert(roleModule);
        } catch (DuplicateKeyException e) {
            throw new ClientException("该角色已授权该模块，请勿重复操作！");
        }
    }

    private void checkRoleThrow(Long roleId) {
        RolePO role = roleMapper.selectById(roleId);
        if(role == null) {
            throw new ServiceException("角色或模块不存在！");
        }
    }
    private void checkModuleThrow(Long moduleId) {
        ModulePO module = moduleMapper.selectById(moduleId);
        if(module == null) {
            throw new ServiceException("角色或模块不存在！");
        }
    }
}
