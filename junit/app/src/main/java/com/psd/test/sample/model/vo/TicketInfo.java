package com.psd.test.sample.model.vo;

import lombok.Data;

/**
 * TicketInfo
 *
 * @author Created by gold on 2021/4/16 16:21
 * @since 1.0.0
 */
@Data
public class TicketInfo {

    /**
     * 票id
     */
    private long ticketId;
    /**
     * 票名称
     */
    private String name;
    /**
     * 票价格
     */
    private long coin;

}
