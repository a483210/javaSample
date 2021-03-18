package com.psd.aop.sample.spring;

/**
 * 接口类
 *
 * @author Created by gold on 2021/3/18 10:29
 */
public interface TicketService {

    /**
     * 买票
     *
     * @param number 票数
     * @return 是否购买成功
     */
    boolean buy(int number);
}

