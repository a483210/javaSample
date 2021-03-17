package com.psd.aop.sample.annotation;

import java.lang.annotation.*;

/**
 * 字节码级别注解
 *
 * @author Created by gold on 2021/3/14 15:22
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface PolicyClassAnnotation {

    /**
     * 值
     */
    String value() default "";

}