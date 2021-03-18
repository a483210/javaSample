package com.psd.aop.sample.spring;

/**
 * 实现类
 *
 * @author Created by gold on 2021/3/18 10:31
 */
public class TicketServiceImplProxy extends TicketServiceImpl {

    private final TicketServiceImpl ticketServiceImpl;

    public TicketServiceImplProxy(TicketServiceImpl ticketServiceImpl) {
        this.ticketServiceImpl = ticketServiceImpl;
    }

    @Override
    public boolean buy(int number) {
        //如果被代理则返回结果
        if (isProxy()) {
            return (boolean) getResult();
        }

        return ticketServiceImpl.buy(number);
    }

    private boolean isProxy() {
        return true;
    }

    private Object getResult() {
        return false;
    }
}

