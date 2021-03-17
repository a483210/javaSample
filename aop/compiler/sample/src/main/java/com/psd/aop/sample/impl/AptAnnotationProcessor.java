package com.psd.aop.sample.impl;

import com.psd.aop.sample.annotation.AptClassAnnotation;
import com.psd.aop.sample.annotation.FiledAnnotation;
import com.psd.aop.sample.annotation.MethodAnnotation;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import com.xy.im.base.core.AptProcessor;
import com.xy.im.base.core.RoundProcessor;
import com.xy.im.base.utils.ElementUtils;
import com.xy.im.base.utils.ProcessorUtils;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * AptAnnotationProcessor
 *
 * @author Created by gold on 2021/3/14 18:06
 */
public class AptAnnotationProcessor extends AptProcessor {

    public static AptAnnotationProcessor create(ProcessingEnvironment processingEnv) {
        return new AptAnnotationProcessor(processingEnv);
    }

    public AptAnnotationProcessor(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public String registerAnnotationType() {
        return AptClassAnnotation.class.getCanonicalName();
    }

    @Override
    public void process(RoundProcessor roundEnv) {
        Set<? extends Element> elements = roundEnv.getRoundEnv().getElementsAnnotatedWith(AptClassAnnotation.class);
        if (ObjectUtils.isEmpty(elements)) {
            return;
        }

        elements.forEach(element -> {
            AptClassAnnotation classAnnotation = element.getAnnotation(AptClassAnnotation.class);

            String clsName = element.getSimpleName().toString();
            String path = element.getEnclosingElement().toString();
            String packName = String.format("%sImpl", clsName);

            List<FieldSpec> fieldSpecs = generateFiledSpecs(element);
            List<MethodSpec> methodSpecs = generateMethodSpecs(element);

            TypeSpec typeSpec = TypeSpec.classBuilder(ClassName.bestGuess(packName))
                    .addJavadoc(String.format("AptClassAnnotation的value=%s", classAnnotation.value()))
                    //继承原对象
                    .superclass(ClassName.get(element.asType()))
                    //添加修饰符
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    //添加变量
                    .addFields(fieldSpecs)
                    //添加方法
                    .addMethods(methodSpecs)
                    .build();

            //创建类文件
            ProcessorUtils.createClass(path, typeSpec);
        });
    }

    private List<FieldSpec> generateFiledSpecs(Element element) {
        List<? extends Element> members = elementsEnv.getAllMembers(((TypeElement) element));
        List<VariableElement> variableElements = ElementUtils.searchFiled(members);

        List<FieldSpec> specs = new ArrayList<>(variableElements.size());

        for (VariableElement ve : variableElements) {
            FiledAnnotation filedAnnotation = ve.getAnnotation(FiledAnnotation.class);
            if (filedAnnotation == null) {
                continue;
            }

            String vName = ve.getSimpleName().toString();
            String filedName = String.format("%sImpl", vName);

            //构建变量配置
            FieldSpec fieldSpec = FieldSpec.builder(ClassName.get(ve.asType()), filedName)
                    .addModifiers(Modifier.PRIVATE)
                    //初始值
                    .initializer("$S", String.format("value=%s", filedAnnotation.value()))
                    .build();

            specs.add(fieldSpec);
        }

        return specs;
    }

    private List<MethodSpec> generateMethodSpecs(Element element) {
        List<? extends Element> members = elementsEnv.getAllMembers(((TypeElement) element));
        List<ExecutableElement> executableElements = ElementUtils.searchMethod(members);

        List<MethodSpec> specs = new ArrayList<>(executableElements.size());

        for (ExecutableElement ee : executableElements) {
            MethodAnnotation methodAnnotation = ee.getAnnotation(MethodAnnotation.class);
            if (methodAnnotation == null) {
                continue;
            }

            String eName = ee.getSimpleName().toString();

            //构建方法配置
            MethodSpec methodSpec = MethodSpec.methodBuilder(eName)
                    .addModifiers(Modifier.PUBLIC)
                    //添加注解
                    .addAnnotation(Override.class)
                    //返回值
                    .returns(ClassName.get(ee.getReturnType()))
                    //添加表达式
                    .addStatement("return $S", String.format("apt value=%s", methodAnnotation.value()))
                    .build();

            specs.add(methodSpec);
        }

        return specs;
    }
}