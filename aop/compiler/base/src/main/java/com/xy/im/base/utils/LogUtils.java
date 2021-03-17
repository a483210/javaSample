package com.xy.im.base.utils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/**
 * log打印
 *
 * @author Created by gold on 2019-08-30 17:59
 */
public final class LogUtils {
    private LogUtils() {
    }

    private static ProcessingEnvironment processingEnv;

    public static void init(ProcessingEnvironment env) {
        processingEnv = env;
    }

    public static void logError(Element element, String str) {
        log(Diagnostic.Kind.ERROR, element, str);
    }

    public static void logError(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, str);
    }

    public static void log(String str) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, str);
    }

    public static void log(Diagnostic.Kind kind, Element element, String str) {
        processingEnv.getMessager().printMessage(kind, str, element);
    }
}
