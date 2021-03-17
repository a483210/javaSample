package com.psd.aop.sample.annotation;

import java.lang.annotation.*;

/**
 * asm类注解
 *
 * @author Created by gold on 2021/3/15 10:31
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AsmClassAnnotation {

    /**
     * 值
     */
    String value();

}
