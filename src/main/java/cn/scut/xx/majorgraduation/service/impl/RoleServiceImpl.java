package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.scut.xx.majorgraduation.core.exception.ExceptionUtil;
import cn.scut.xx.majorgraduation.core.exception.ServiceException;
import cn.scut.xx.majorgraduation.dao.mapper.RoleMapper;
import cn.scut.xx.majorgraduation.dao.po.RolePO;
import cn.scut.xx.majorgraduation.pojo.dto.req.RoleSaveReqDTO;
import cn.scut.xx.majorgraduation.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author 徐鑫
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, RolePO> implements IRoleService {
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
}
