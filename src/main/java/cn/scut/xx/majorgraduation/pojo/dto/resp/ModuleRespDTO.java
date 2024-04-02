package cn.scut.xx.majorgraduation.pojo.dto.resp;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class ModuleRespDTO {
    /**
     * 主键
     */
    private Long moduleId;

    /**
     * 编号（对外）
     */
    private Long code;

    /**
     * 模块名
     */
    private String name;

    /**
     * 模块状态，请见系统字典kind=4
     */
    private Integer status;

    /**
     * 模块所对应的url
     */
    private String url;

    /**
     * 上层模块id
     */
    private Long upperModuleId;

    /**
     * 模块层级，用于前端显示
     */
    private Integer moduleLevel;
}
