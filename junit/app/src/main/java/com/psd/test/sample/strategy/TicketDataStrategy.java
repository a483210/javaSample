package com.psd.test.sample.strategy;

import com.psd.test.sample.model.vo.TicketInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 票数据策略
 *
 * @author Created by gold on 2021/4/16 16:24
 * @since 1.0.0
 */
public interface TicketDataStrategy {

    /**
     * 获取票信息
     *
     * @param ticketId 票id
     * @return 票信息
     */
    TicketInfo getTicket(long ticketId);

    class SampleTicketDataStrategy implements TicketDataStrategy {

        private final Map<Long, TicketInfo> cache = new HashMap<>();

        @Override
        public TicketInfo getTicket(long ticketId) {
            if (ticketId > 1000L) {
                throw new IllegalArgumentException("机票不存在");
            }

            return cache.computeIfAbsent(ticketId, id -> {
                TicketInfo info = new TicketInfo();

                info.setTicketId(id);
                info.setName("虚拟机票" + id);
                info.setCoin((long) (Math.random() * 1000 + 1000));

                return info;
            });
        }
    }
}
