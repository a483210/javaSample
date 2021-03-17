package com.psd.aop.sample;

import com.psd.aop.sample.impl.AptAnnotationProcessor;
import com.psd.aop.sample.impl.AstAnnotationProcessor;
import com.xy.im.base.AnnotationProcessor;
import com.xy.im.base.core.IProcessor;

import java.util.Arrays;
import java.util.List;

/**
 * 扫描工程，生成代码
 *
 * @author Created by gold on 2021/3/14 18:09
 */
public class SampleAnnotationProcessor extends AnnotationProcessor {

    @Override
    protected List<IProcessor> registerProcessor() {
        return Arrays.asList(AptAnnotationProcessor.create(processingEnv),
                AstAnnotationProcessor.create(processingEnv));
    }

}