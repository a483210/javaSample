package com.psd.test.sample.utils;

import java.lang.reflect.Type;

/**
 * LoadUtils
 *
 * @author Created by gold on 2021/4/17 10:57
 * @since 1.0.0
 */
public class LoadUtils {
    private LoadUtils() {
    }

    /**
     * 是否为支持的类型
     *
     * @param type 类型
     * @return bool
     */
    public static boolean isSupportedTypes(Type type) {
        if (!(type instanceof Class<?>)) {
            return false;
        }

        return formatType((Class<?>) type).isPrimitive();
    }

    /**
     * 转换原始对象类型为基本类型
     *
     * @param type 原始对象类型
     */
    public static Class<?> formatType(Class<?> type) {
        if (type == Byte.class) {
            return Byte.TYPE;
        } else if (type == Short.class) {
            return Short.TYPE;
        } else if (type == Integer.class) {
            return Integer.TYPE;
        } else if (type == Long.class) {
            return Long.TYPE;
        } else if (type == Float.class) {
            return Float.TYPE;
        } else if (type == Double.class) {
            return Double.TYPE;
        } else if (type == Boolean.class) {
            return Boolean.TYPE;
        } else if (type == Character.class) {
            return Character.TYPE;
        } else {
            return type;
        }
    }
}
