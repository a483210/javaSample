package com.psd.test.sample.component.manager;

import com.psd.test.sample.component.application.Application;
import com.psd.test.sample.utils.LoadUtils;

/**
 * LoadManager
 *
 * @author Created by gold on 2021/4/17 10:56
 * @since 1.0.0
 */
public class LoadManager {

    private static volatile LoadManager instance;

    public static LoadManager get() {
        if (instance == null) {
            synchronized (LoadManager.class) {
                if (instance == null) {
                    instance = new LoadManager();
                }
            }
        }
        return instance;
    }

    private final Application application;

    private LoadManager() {
        this.application = new Application();
    }

    public <T> String mapper(T t) {
        if (!LoadUtils.isSupportedTypes(t.getClass())) {
            throw new IllegalArgumentException("不支持的类型");
        }
        return application.mapper(t);
    }
}