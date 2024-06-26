package cn.scut.xx.majorgraduation.service;

import cn.scut.xx.majorgraduation.pojo.dto.req.*;
import cn.scut.xx.majorgraduation.pojo.dto.resp.LoginRespDTO;
import cn.scut.xx.majorgraduation.pojo.dto.resp.UserRespDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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
     * 给某个用户添加角色信息
     *
     * @param userRoleAddReqDTO 需要添加的信息
     */
    void addRole(UserRoleAddReqDTO userRoleAddReqDTO);

    /**
     * 移除用户的角色
     *
     * @param userRoleRemoveReqDTO 需要移除的信息
     */
    void removeRole(UserRoleRemoveReqDTO userRoleRemoveReqDTO);

    /**
     * 查找用户数据
     *
     * @param userSearchReqDTO 查询条件
     * @return 分页结果集
     */
    Page<UserRespDTO> searchList(UserSearchReqDTO userSearchReqDTO);

    /**
     * 用户登录
     *
     * @param userLoginReqDTO 登录参数
     * @return token
     */
    LoginRespDTO login(UserLoginReqDTO userLoginReqDTO);

    /**
     * 从token中获取用户信息
     *
     * @param token token
     * @return user info
     */
    UserRespDTO getUserInfoFromToken(String token);

    /**
     * 修改用户信息
     *
     * @param userUpdateReqDTO 新数据
     */
    void updateUserInfo(UserUpdateReqDTO userUpdateReqDTO);

    /**
     * 根据id删除用户
     *
     * @param userId 用户id
     */
    void deleteUser(Long userId);

    /**
     * 修改当前用户的个人信息
     *
     * @param userUpdateCurrentInfoReqDTO 需要修改成的信息
     */
    void updateCurrent(UserUpdateCurrentInfoReqDTO userUpdateCurrentInfoReqDTO);

    /**
     * 刷新token
     *
     * @param flushToken flush token
     * @return token
     */
    String flushToken(String flushToken);

    /**
     * 检测手机号是否已存在
     *
     * @param phoneNumber 待检测的手机号
     * @return 检测结果
     */
    boolean checkPhoneExist(String phoneNumber);

    /**
     * 检测用户身份证号是否已注册
     *
     * @param idCard 身份证号
     * @return 检测结果
     */
    boolean checkIdCardExist(String idCard);
}
