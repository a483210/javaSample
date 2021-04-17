package com.psd.test.sample.config;

import com.psd.test.sample.strategy.TicketDataStrategy;
import com.psd.test.sample.strategy.UserDataStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * StrategyConfiguration
 *
 * @author Created by gold on 2021/4/16 16:24
 * @since 1.0.0
 */
@Configuration
public class StrategyConfiguration {

    @Bean
    public UserDataStrategy userDataStrategy() {
        return new UserDataStrategy.SampleUserDataStrategy();
    }

    @Bean
    public TicketDataStrategy ticketDataStrategy() {
        return new TicketDataStrategy.SampleTicketDataStrategy();
    }
}
