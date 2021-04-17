package com.psd.test.sample.strategy;

import com.psd.test.sample.model.vo.UserInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户数据策略
 *
 * @author Created by gold on 2021/4/16 16:20
 * @since 1.0.0
 */
public interface UserDataStrategy {

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    UserInfo getUser(long userId);

    class SampleUserDataStrategy implements UserDataStrategy {

        private final Map<Long, UserInfo> cache = new HashMap<>();

        @Override
        public UserInfo getUser(long userId) {
            return cache.computeIfAbsent(userId, id -> {
                UserInfo userInfo = new UserInfo();

                userInfo.setUserId(id);
                userInfo.setNickname("虚拟用户" + id);
                userInfo.setCoin((long) (Math.random() * 10000L + 10000L));

                return userInfo;
            });
        }
    }
}
