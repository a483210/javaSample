package com.psd.aop.sample.annotation;

import java.lang.annotation.*;

/**
 * 源码级别注释
 *
 * @author Created by gold on 2021/3/14 15:21
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface PolicySourceAnnotation {

    /**
     * 值
     */
    String value() default "";

}
