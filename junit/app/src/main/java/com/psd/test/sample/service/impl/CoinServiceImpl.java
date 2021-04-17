package com.psd.test.sample.service.impl;

import com.psd.test.sample.model.vo.UserInfo;
import com.psd.test.sample.service.CoinService;
import com.psd.test.sample.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * CoinServiceImpl
 *
 * @author Created by gold on 2021/4/16 16:08
 * @since 1.0.0
 */
@AllArgsConstructor
@Service
public class CoinServiceImpl implements CoinService {

    private final UserService userService;

    @Override
    public long increment(long userId, long coin) {
        if (coin >= 50 * 10000) {
            throw new IllegalArgumentException("超过最大一次性添加金额");
        }

        UserInfo userInfo = userService.findUserById(userId);
        if (userInfo == null) {
            throw new NullPointerException("用户不存在");
        }

        long totalCoin = userInfo.getCoin() + coin;
        userInfo.setCoin(totalCoin);

        return totalCoin;
    }

    @Override
    public long decrement(long userId, long coin) {
        UserInfo userInfo = userService.findUserById(userId);
        if (userInfo == null) {
            throw new NullPointerException("用户不存在");
        }

        long totalCoin = userInfo.getCoin() - coin;
        if (totalCoin < 0) {
            throw new IllegalStateException("余额不足");
        }

        userInfo.setCoin(totalCoin);

        return totalCoin;
    }
}