package com.xy.im.base.utils;

import com.xy.im.base.error.ProcessorException;

import javax.lang.model.element.Element;

/**
 * 异常工具类
 *
 * @author Created by gold on 2019/9/26 15:12
 */
public final class ErrorUtils {
    private ErrorUtils() {
    }

    public static void throwProcessor(Element element) {
        throwProcessor(element, String.format("不受支持的类型[%s]。", element.asType().toString()));
    }

    public static void throwProcessorAutoParse(Element element) {
        throwProcessor(element, String.format("未被AutoParse标记的类型[%s]", element.asType().toString()));
    }

    public static void throwProcessor(Element element, String str) {
        throw new ProcessorException(element, str);
    }

}
