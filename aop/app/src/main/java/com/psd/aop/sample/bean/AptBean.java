package com.psd.aop.sample.bean;

import com.psd.aop.sample.annotation.AptClassAnnotation;
import com.psd.aop.sample.annotation.FiledAnnotation;
import com.psd.aop.sample.annotation.MethodAnnotation;

/**
 * AptBean
 *
 * @author Created by gold on 2021/3/14 15:24
 */
@AptClassAnnotation("类")
public class AptBean {

    @FiledAnnotation("变量")
    private final String params = "AptBean";

    @MethodAnnotation("方法")
    public String getParams() {
        return params;
    }
}