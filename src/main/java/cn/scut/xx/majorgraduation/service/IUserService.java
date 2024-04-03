package cn.scut.xx.majorgraduation.service;

import cn.scut.xx.majorgraduation.pojo.dto.req.UserSaveReqDTO;

/**
 * @author 徐鑫
 */
public interface IUserService {
    /**
     * 新增用户信息（即创建账号）
     *
     * @param userSaveReqDTO 用户信息参数
     */
    void save(UserSaveReqDTO userSaveReqDTO);


    /**
     * 检查userName是否存在
     *
     * @param userName 待检测的userName
     * @return 如果不存在返回true
     */
    boolean checkUserNameIfNot(String userName);
}
