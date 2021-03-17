package com.psd.aop.sample.bean;

import com.psd.aop.sample.annotation.FiledAnnotation;
import com.psd.aop.sample.annotation.MethodAnnotation;

/**
 * JavassistBean
 *
 * @author Created by gold on 2021/3/14 19:31
 */
public class JavassistBean {

    @FiledAnnotation("变量")
    private final String params = "JavassistBean";

    @MethodAnnotation("方法")
    public String getParams() {
        return params;
    }
}