package com.xy.im.base.core;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

/**
 * apt处理器
 *
 * @author Created by gold on 2019-09-05 11:49
 */
public abstract class AptProcessor implements IProcessor {

    protected ProcessingEnvironment processingEnv;
    protected Elements elementsEnv;
    protected Types typesEnv;
    protected Filer filerEnv;

    public AptProcessor(ProcessingEnvironment processingEnv) {
        this.processingEnv = processingEnv;

        this.elementsEnv = processingEnv.getElementUtils();
        this.typesEnv = processingEnv.getTypeUtils();
        this.filerEnv = processingEnv.getFiler();
    }

}
