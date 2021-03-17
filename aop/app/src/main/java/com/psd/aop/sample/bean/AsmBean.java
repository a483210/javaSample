package com.psd.aop.sample.bean;

import com.psd.aop.sample.annotation.AsmClassAnnotation;
import com.psd.aop.sample.annotation.FiledAnnotation;
import com.psd.aop.sample.annotation.MethodAnnotation;

/**
 * AsmBean
 *
 * @author Created by gold on 2021/3/14 19:31
 */
@AsmClassAnnotation("类")
public class AsmBean {

    @FiledAnnotation("变量")
    private final String params = "AsmBean";

    @MethodAnnotation("方法")
    public String getParams() {
        return params;
    }
}