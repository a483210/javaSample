package com.psd.aop.sample.annotation;

import java.lang.annotation.*;

/**
 * apt类注解
 *
 * @author Created by gold on 2021/3/14 18:36
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AptClassAnnotation {

    /**
     * 值
     */
    String value();

}
