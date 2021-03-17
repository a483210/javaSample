package com.psd.aop.sample.impl;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.psd.aop.sample.annotation.AstClassAnnotation;
import com.psd.aop.sample.annotation.FiledAnnotation;
import com.psd.aop.sample.annotation.MethodAnnotation;
import com.sun.tools.javac.tree.JCTree;
import com.xy.im.base.core.AstProcessor;
import com.xy.im.base.core.RoundProcessor;
import com.xy.im.base.error.ProcessorException;
import com.xy.im.base.jctree.JCBlock;
import com.xy.im.base.jctree.JCUtil;
import com.xy.im.base.utils.ElementUtils;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * AstAnnotationProcessor
 *
 * @author Created by gold on 2021/3/14 18:06
 */
public class AstAnnotationProcessor extends AstProcessor {

    public static AstAnnotationProcessor create(ProcessingEnvironment processingEnv) {
        return new AstAnnotationProcessor(processingEnv);
    }

    public AstAnnotationProcessor(ProcessingEnvironment processingEnv) {
        super(processingEnv);
    }

    @Override
    public String registerAnnotationType() {
        return AstClassAnnotation.class.getCanonicalName();
    }

    @Override
    public void process(RoundProcessor roundEnv) {
        Set<? extends Element> elements = roundEnv.getRoundEnv().getElementsAnnotatedWith(AstClassAnnotation.class);
        if (ObjectUtils.isEmpty(elements)) {
            return;
        }

        elements.forEach(element -> {
            JCTree.JCCompilationUnit tree = (JCTree.JCCompilationUnit) treesEnv.getPath(element).getCompilationUnit();

            CompilationUnit compilationUnit = StaticJavaParser.parse(tree.toString());
            if (compilationUnit == null) {
                throw new ProcessorException(element, "无法被解析为语法树！");
            }

            Optional<ClassOrInterfaceDeclaration> optional = compilationUnit.getClassByName(element.getSimpleName().toString());
            if (!optional.isPresent()) {
                throw new ProcessorException(element, "无法搜索到主类！");
            }

            ClassOrInterfaceDeclaration declaration = optional.get();

            //修改语法树开始
            AstClassAnnotation classAnnotation = element.getAnnotation(AstClassAnnotation.class);

            changeFiledSpecs(element, declaration);
            changeMethodSpecs(element, declaration);

            JCUtil.writeTo(context, tree, compilationUnit);
        });
    }

    private void changeFiledSpecs(Element element, ClassOrInterfaceDeclaration declaration) {
        List<? extends Element> members = elementsEnv.getAllMembers(((TypeElement) element));
        List<VariableElement> variableElements = ElementUtils.searchFiled(members);

        for (VariableElement ve : variableElements) {
            FiledAnnotation filedAnnotation = ve.getAnnotation(FiledAnnotation.class);
            if (filedAnnotation == null) {
                continue;
            }

            String vName = ve.getSimpleName().toString();

            //获取变量描述
            FieldDeclaration fieldDeclaration = declaration.getFieldByName(vName).orElse(null);
            if (fieldDeclaration == null) {
                throw new ProcessorException(element, "未知异常，无法找到变量！");
            }

            fieldDeclaration.getVariables()
                    .get(0)
                    //修改初始化值
                    .setInitializer(String.format("\"value=%s\"", filedAnnotation.value()));
        }
    }

    private void changeMethodSpecs(Element element, ClassOrInterfaceDeclaration declaration) {
        List<? extends Element> members = elementsEnv.getAllMembers(((TypeElement) element));
        List<ExecutableElement> executableElements = ElementUtils.searchMethod(members);

        for (ExecutableElement ee : executableElements) {
            MethodAnnotation methodAnnotation = ee.getAnnotation(MethodAnnotation.class);
            if (methodAnnotation == null) {
                continue;
            }

            String eName = ee.getSimpleName().toString();

            //获取方法描述
            MethodDeclaration methodDeclaration = declaration.getMethodsByName(eName).get(0);
            if (methodDeclaration == null) {
                throw new ProcessorException(element, "未知异常，无法找到变量！");
            }

            //构建表达式
            BlockStmt blockStmt = JCBlock.create()
                    .addStatement("return $S", String.format("ast value=%s", methodAnnotation.value()))
                    .generate();

            //修改方法体
            methodDeclaration.setBody(blockStmt);
        }
    }
}