package com.psd.test.sample.component.application;

/**
 * Application
 *
 * @author Created by gold on 2021/4/17 10:29
 * @since 1.0.0
 */
public class Application {

    private final Parser parser;

    public Application() {
        this.parser = new Parser();
    }

    public <T> String mapper(T t) {
        return parser.mapper(t);
    }
}
