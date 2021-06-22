package com.leyou.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leyou.user.dto.UserDTO;
import com.leyou.user.entity.User;

/**
 * @author Huang Mingwang
 * @create 2021-06-05 5:37 下午
 */
public interface UserService extends IService<User>{
    /**
     * 发送注册短信
     * @param phone 电话号码
     */
    void sendVerifyCode(String phone);

    /**
     * 查询数据是否存在
     * @param data 数据
     * @param type 类型
     * @return Boolean
     */
    Boolean infoExists(String data, Integer type);

    /**
     * 用户组册
     * @param user 用户信息
     * @param code 验证码
     */
    void register(User user, String code);

    /**
     *
     * @param username 用户名
     * @param password 密码
     * @return UserDTO
     */
    UserDTO queryUserByNameAndPassword(String username, String password);
}
