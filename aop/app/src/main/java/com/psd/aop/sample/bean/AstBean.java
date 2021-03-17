package com.psd.aop.sample.bean;

import com.psd.aop.sample.annotation.AstClassAnnotation;
import com.psd.aop.sample.annotation.FiledAnnotation;
import com.psd.aop.sample.annotation.MethodAnnotation;

/**
 * AstBean
 *
 * @author Created by gold on 2021/3/14 15:28
 */
@AstClassAnnotation("类")
public class AstBean {

    @FiledAnnotation("变量")
    private final String params = "AstBean";

    @MethodAnnotation("方法")
    public String getParams() {
        return params;
    }
}