package com.xy.im.base.utils;

import java.util.Collection;
import java.util.Map;

/**
 * 文本工具类
 *
 * @author Created by gold on 2019-08-27 18:17
 */
public final class TextUtils {
    private TextUtils() {
    }

    /**
     * 将第一个转换为小写
     *
     * @param str 字符串
     */
    public static String toLowerCase(String str) {
        return toLowerCase(str, 1);
    }

    /**
     * 将前几个转换为小写
     *
     * @param str    字符串
     * @param length 长度
     */
    public static String toLowerCase(String str, int length) {
        if (isEmpty(str) || length <= 0) {
            return str;
        }
        if (length == str.length()) {
            return str.toLowerCase();
        }

        char[] chars = str.toCharArray();
        for (int i = 0; i < length; i++) {
            chars[i] = Character.toLowerCase(chars[i]);
        }

        return new String(chars);
    }

    /**
     * 将第一个转换为大写
     *
     * @param str 字符串
     */
    public static String toUpperCase(String str) {
        return toUpperCase(str, 1);
    }

    /**
     * 将前几个转换为大写
     *
     * @param str    字符串
     * @param length 长度
     */
    public static String toUpperCase(String str, int length) {
        if (isEmpty(str) || length <= 0) {
            return str;
        }
        if (length == str.length()) {
            return str.toUpperCase();
        }

        char[] chars = str.toCharArray();
        for (int i = 0; i < length; i++) {
            chars[i] = Character.toUpperCase(chars[i]);
        }

        return new String(chars);
    }

    /**
     * 判断是否为空串
     *
     * @param str 字符串
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 是否为空
     *
     * @param collection collection
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * 是否为空
     *
     * @param map map
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

}
