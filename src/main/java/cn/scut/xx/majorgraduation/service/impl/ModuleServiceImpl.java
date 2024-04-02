package cn.scut.xx.majorgraduation.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.scut.xx.majorgraduation.core.exception.ServiceException;
import cn.scut.xx.majorgraduation.dao.mapper.ModuleMapper;
import cn.scut.xx.majorgraduation.dao.po.ModulePO;
import cn.scut.xx.majorgraduation.pojo.dto.req.ModuleSaveReqDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.ModuleRespDTO;
import cn.scut.xx.majorgraduation.service.ModuleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 徐鑫
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ModuleServiceImpl extends ServiceImpl<ModuleMapper, ModulePO> implements ModuleService {
    @Override
    public List<ModuleRespDTO> getAll() {
        List<ModulePO> list = baseMapper.selectList(null);
        return BeanUtil.copyToList(list, ModuleRespDTO.class);
    }

    @Override
    public void save(ModuleSaveReqDTO moduleSaveReqDTO) {
        ModulePO toInsert = BeanUtil.toBean(moduleSaveReqDTO, ModulePO.class);
        if (moduleSaveReqDTO.getUpperModuleId() != null
                && moduleSaveReqDTO.getUpperModuleId() != 0) {
            ModulePO upperModule = this.selectById(moduleSaveReqDTO.getUpperModuleId());
            toInsert.setModuleLevel(upperModule.getModuleLevel() + 1);
        }
        baseMapper.insert(toInsert);
    }

    public ModulePO selectById(Long id) {
        ModulePO upperModule = baseMapper.selectById(id);
        if (upperModule == null) {
            throw new ServiceException("id为" + id + "的模块不存在");
        }
        return upperModule;
    }
}
