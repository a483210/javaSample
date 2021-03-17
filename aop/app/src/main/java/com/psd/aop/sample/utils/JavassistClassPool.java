package com.psd.aop.sample.utils;

import javassist.ClassPool;
import javassist.LoaderClassPath;

import java.util.Comparator;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * 用于加载不同ClassLoader
 *
 * @author Created by gold on 2019/12/26 13:35
 */
public final class JavassistClassPool {
    private JavassistClassPool() {
    }

    private static final Set<ClassLoader> CLASS_LOADERS =
            new ConcurrentSkipListSet<>(Comparator.comparingInt(Object::hashCode));

    /**
     * 获取默认ClassPool
     *
     * @param cls 对象池
     */
    public static ClassPool getClassPool(Class<?> cls) {
        ClassPool pool = ClassPool.getDefault();

        ClassLoader classLoader = cls.getClassLoader();
        if (classLoader != null && CLASS_LOADERS.add(classLoader)) {
            pool.appendClassPath(new LoaderClassPath(classLoader));
        }

        return pool;
    }

    /**
     * 加载class
     *
     * @param pool    pool
     * @param clsPath 地址
     * @return cls
     */
    public static Class<?> loadClass(ClassPool pool, String clsPath) {
        try {
            if (pool.getOrNull(clsPath) != null) {
                return pool.getClassLoader().loadClass(clsPath);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

}
