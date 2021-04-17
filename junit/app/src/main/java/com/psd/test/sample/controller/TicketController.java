package com.psd.test.sample.controller;

import com.psd.test.sample.model.request.TicketBuyRequest;
import com.psd.test.sample.model.result.ServerResult;
import com.psd.test.sample.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TicketController
 *
 * @author Created by gold on 2021/4/16 16:55
 * @since 1.0.0
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/v1/ticket")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/buy")
    public ServerResult<Long> buyTicket(@Validated @RequestBody TicketBuyRequest request) {
        long coin = ticketService.buy(request.getUserId(), request.getTicketId(), request.getNumber());

        return ServerResult.success(coin);
    }
}