package com.psd.aop.sample.spring;

/**
 * 实现类
 *
 * @author Created by gold on 2021/3/18 10:31
 */
public class TicketServiceImpl implements TicketService {

    @Override
    public boolean buy(int number) {
        //买票成功
        return true;
    }
}

