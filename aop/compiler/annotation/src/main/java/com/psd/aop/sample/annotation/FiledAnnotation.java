package com.psd.aop.sample.annotation;

import java.lang.annotation.*;

/**
 * 变量注解
 *
 * @author Created by gold on 2021/3/14 15:21
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FiledAnnotation {

    /**
     * 值
     */
    String value();

}
