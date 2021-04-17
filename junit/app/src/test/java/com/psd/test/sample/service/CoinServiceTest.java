package com.psd.test.sample.service;

import com.psd.test.sample.model.vo.UserInfo;
import com.psd.test.sample.service.impl.CoinServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * CoinServiceTest
 *
 * @author Created by gold on 2021/4/16 16:29
 * @since 1.0.0
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = CoinServiceImpl.class)
public class CoinServiceTest {

    @Autowired
    private CoinService coinService;

    @MockBean
    private UserService userService;

    private long userId;
    private UserInfo userInfo;

    @BeforeEach
    public void setUp() {
        this.userId = 1L;

        this.userInfo = new UserInfo();

        userInfo.setUserId(1);
        userInfo.setNickname("测试用户");
        userInfo.setCoin(1000L);
    }

    /**
     * 测试正常加钱
     */
    @Test
    public void testIncrement() {
        when(userService.findUserById(userId)).thenReturn(userInfo);

        coinService.increment(userId, 1000);

        assertThat(userInfo.getCoin())
                .isEqualTo(2000L);

        verify(userService).findUserById(userId);
    }

    /**
     * 测试用户为空
     */
    @Test
    public void testIncrementByNilUser() {
        assertThatThrownBy(() -> {
            coinService.increment(userId, 1000);
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("不存在");
    }

    /**
     * 测试超大额添加
     */
    @Test
    public void testIncrementByLargeAmount() {
        assertThatThrownBy(() -> {
            coinService.increment(userId, 500 * 10000L);
        })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("金额");

        assertThat(userInfo.getCoin())
                .isEqualTo(1000L);
    }

    /**
     * 测试正常扣费
     */
    @Test
    public void testDecrement() {
        when(userService.findUserById(userId)).thenReturn(userInfo);

        coinService.decrement(userId, 500);

        assertThat(userInfo.getCoin())
                .isEqualTo(500L);

        coinService.decrement(userId, 500);

        assertThat(userInfo.getCoin())
                .isEqualTo(0L);

        verify(userService, times(2)).findUserById(userId);
    }

    /**
     * 测试用户为空
     */
    @Test
    public void testDecrementByNilUser() {
        assertThatThrownBy(() -> {
            coinService.decrement(userId, 1000);
        })
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("不存在");
    }

    /**
     * 测试余额不足
     */
    @Test
    public void testIncrementByNotEnough() {
        when(userService.findUserById(userId)).thenReturn(userInfo);

        assertThatThrownBy(() -> {
            coinService.decrement(userId, 2000L);
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("余额不足");

        assertThat(userInfo.getCoin())
                .isEqualTo(1000L);

        verify(userService).findUserById(userId);
    }
}