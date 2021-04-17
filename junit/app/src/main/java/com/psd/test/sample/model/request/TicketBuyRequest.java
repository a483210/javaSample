package com.psd.test.sample.model.request;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * TicketBuyRequest
 *
 * @author Created by gold on 2021/4/16 17:00
 * @since 1.0.0
 */
@Data
public class TicketBuyRequest {

    /**
     * 用户id
     */
    @NotNull
    private Long userId;
    /**
     * 票id
     */
    @NotNull
    private Long ticketId;
    /**
     * 购买数量
     */
    @NotNull
    @Min(1)
    private Integer number = 1;

}