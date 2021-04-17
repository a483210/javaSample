package com.psd.test.sample.service;

/**
 * CoinService
 *
 * @author Created by gold on 2021/4/16 16:08
 * @since 1.0.0
 */
public interface CoinService {

    /**
     * 添加金额
     *
     * @param userId 用户id
     * @param coin   金额
     * @return 剩余金额
     */
    long increment(long userId, long coin);

    /**
     * 减少金额
     *
     * @param userId 用户id
     * @param coin   金额
     * @return 剩余金额
     */
    long decrement(long userId, long coin);

}
