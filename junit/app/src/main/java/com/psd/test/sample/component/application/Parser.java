package com.psd.test.sample.component.application;

/**
 * Parser
 *
 * @author Created by gold on 2021/4/17 10:29
 * @since 1.0.0
 */
public class Parser {

    /**
     * 映射
     *
     * @param t src
     * @return dst
     */
    public <T> String mapper(T t) {
        if (t instanceof String) {
            return (String) t;
        }
        return String.valueOf(t);
    }
}