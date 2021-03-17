package com.psd.aop.sample.asm.error;

/**
 * AsmException
 *
 * @author Created by gold on 2021/3/15 10:17
 */
class AsmException extends RuntimeException {
    AsmException() {
    }

    AsmException(String message) {
        super(message)
    }

    AsmException(String message, Throwable throwable) {
        super(message, throwable)
    }

    AsmException(Throwable throwable) {
        super(throwable)
    }
}
