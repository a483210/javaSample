package com.psd.aop.sample.asm.utils

/**
 * TextUtils
 *
 * @author Created by gold on 2021/3/15 10:19
 */
final class TextUtils {
    private TextUtils() {
    }

    /**
     * 判断是否为空串
     *
     * @param str 字符串
     */
    static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 判断是否为空array
     *
     * @param collection array
     */
    static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
