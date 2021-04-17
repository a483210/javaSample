package com.psd.test.sample.service;

import com.psd.test.sample.model.vo.UserInfo;

/**
 * UserService
 *
 * @author Created by gold on 2021/4/16 16:08
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    UserInfo findUserById(long userId);
}
