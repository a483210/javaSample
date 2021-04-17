package com.psd.test.sample.service.impl;

import com.psd.test.sample.model.vo.TicketInfo;
import com.psd.test.sample.service.CoinService;
import com.psd.test.sample.service.TicketService;
import com.psd.test.sample.strategy.TicketDataStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * TicketServiceImpl
 *
 * @author Created by gold on 2021/4/16 16:02
 * @since 1.0.0
 */
@AllArgsConstructor
@Service
public class TicketServiceImpl implements TicketService {

    private final CoinService coinService;

    private final TicketDataStrategy strategy;

    @Override
    public long buy(long userId, long ticketId, int number) {
        TicketInfo ticketInfo = strategy.getTicket(ticketId);
        if (ticketInfo == null) {
            throw new NullPointerException("机票不存在");
        }

        long consume = number * ticketInfo.getCoin();

        return coinService.decrement(userId, consume);
    }
}