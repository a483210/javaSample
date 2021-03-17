package com.xy.im.base;

import com.xy.im.base.core.IProcessor;
import com.xy.im.base.core.RoundProcessor;
import com.xy.im.base.error.ProcessorException;
import com.xy.im.base.utils.LogUtils;
import com.xy.im.base.utils.ProcessorUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;

/**
 * 扫描工程，生成代码
 *
 * @author Created by gold on 2019-08-30 17:45
 */
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public abstract class AnnotationProcessor extends AbstractProcessor {

    private boolean initializedProcessor;

    private List<IProcessor> processors;
    private Set<String> annotationTypes;
    private Map<String, Object> attributes;

    @Override
    public final synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        this.processors = registerProcessor();

        initAnnotationTypes();
        initAttributes();

        ProcessorUtils.init(processingEnv);
        LogUtils.init(processingEnv);
    }

    private void initAnnotationTypes() {
        this.annotationTypes = new HashSet<>();

        for (IProcessor processor : processors) {
            annotationTypes.add(processor.registerAnnotationType());
        }
    }

    private void initAttributes() {
        this.attributes = new HashMap<>();
    }

    protected void setAttributes(String name, Object value) {
        attributes.put(name, value);
    }

    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        initProcess(annotations, roundEnv);

        Map<String, List<Object>> params = new HashMap<>();

        try {
            for (IProcessor processor : processors) {
                processor.process(new RoundProcessor(roundEnv, annotations, attributes, params));
            }
        } catch (ProcessorException e) {
            if (e.getElement() == null) {
                LogUtils.logError(e.getMessage());
            } else {
                LogUtils.logError(e.getElement(), e.getMessage());
            }
            return false;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }

        return true;
    }

    @Override
    public final Set<String> getSupportedAnnotationTypes() {
        return annotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void initProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (!initializedProcessor) {
            initProcessor(roundEnv);
            initializedProcessor = true;
        }
    }

    protected void initProcessor(RoundEnvironment roundEnv) {
    }

    protected abstract List<IProcessor> registerProcessor();

}