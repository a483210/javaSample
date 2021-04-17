package com.psd.test.sample.service;

/**
 * TicketService
 *
 * @author Created by gold on 2021/4/16 16:01
 * @since 1.0.0
 */
public interface TicketService {

    /**
     * 买票
     *
     * @param userId   用户id
     * @param ticketId 票id
     * @param number   票数
     * @return coin 用户剩余金额
     */
    long buy(long userId, long ticketId, int number);

}
