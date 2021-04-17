package com.psd.test.sample.service;

import com.psd.test.sample.model.vo.TicketInfo;
import com.psd.test.sample.service.impl.TicketServiceImpl;
import com.psd.test.sample.strategy.TicketDataStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * TicketServiceTest
 *
 * @author Created by gold on 2021/4/16 16:43
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TicketServiceImpl.class)
public class TicketServiceTest {

    @Autowired
    private TicketService ticketService;

    @MockBean
    private CoinService coinService;
    @MockBean
    private TicketDataStrategy ticketDataStrategy;

    private long userId;

    private long ticketId;
    private TicketInfo ticketInfo;

    @BeforeEach
    public void setUp() {
        this.userId = 1L;

        this.ticketId = 500;

        this.ticketInfo = new TicketInfo();

        ticketInfo.setTicketId(ticketId);
        ticketInfo.setName("测试机票" + ticketId);
        ticketInfo.setCoin(100L);
    }

    /**
     * 测试正常买票
     */
    @Test
    public void testBuy() {
        when(ticketDataStrategy.getTicket(ticketId)).thenReturn(ticketInfo);
        when(coinService.decrement(eq(userId), anyLong())).thenReturn(500L);

        long coin = ticketService.buy(userId, ticketId, 5);

        assertThat(coin)
                .isEqualTo(500L);

        verify(coinService).decrement(anyLong(), anyLong());
    }

    /**
     * 测试票为空
     */
    @Test
    public void testBuyByNilTicket() {
        assertThatThrownBy(() -> {
            ticketService.buy(userId, ticketId, 5);
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("不存在");

        verify(coinService, never()).decrement(anyLong(), anyLong());
    }
}