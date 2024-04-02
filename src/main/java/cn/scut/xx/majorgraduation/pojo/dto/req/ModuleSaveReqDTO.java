package cn.scut.xx.majorgraduation.pojo.dto.req;

import lombok.Data;

/**
 * @author 徐鑫
 */
@Data
public class ModuleSaveReqDTO {

    /**
     * 编号（对外）
     */
    private Long code;

    /**
     * 模块名
     */
    private String name;

    /**
     * 模块所对应的url
     */
    private String url;

    /**
     * 上层模块id
     */
    private Long upperModuleId;
}
