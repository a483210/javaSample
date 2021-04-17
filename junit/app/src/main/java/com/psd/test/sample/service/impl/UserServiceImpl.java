package com.psd.test.sample.service.impl;

import com.psd.test.sample.model.vo.UserInfo;
import com.psd.test.sample.service.UserService;
import com.psd.test.sample.strategy.UserDataStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * UserServiceImpl
 *
 * @author Created by gold on 2021/4/16 16:09
 * @since 1.0.0
 */
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserDataStrategy userDataStrategy;

    @Override
    public UserInfo findUserById(long userId) {
        return userDataStrategy.getUser(userId);
    }
}