package com.psd.aop.sample.annotation;

import java.lang.annotation.*;

/**
 * 方法注解
 *
 * @author Created by gold on 2021/3/14 15:19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MethodAnnotation {

    /**
     * 值
     */
    String value();

}
