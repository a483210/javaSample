package com.psd.aop.sample.utils;

import com.psd.aop.sample.annotation.MethodAnnotation;
import com.xy.im.base.utils.StatementUtils.CodeBuilder;
import javassist.*;

import java.lang.reflect.Method;

/**
 * JavassistUtils
 *
 * @author Created by gold on 2021/3/14 19:07
 */
public class JavassistUtils {
    private JavassistUtils() {
    }

    /**
     * 创建代理类
     *
     * @param clazz 类型
     * @return 实例
     */
    public static <T> T createProxyObject(Class<T> clazz) {
        try {
            Class<?> newClass = createClass(clazz);
            if (newClass == null) {
                return null;
            }

            //反射创建类
            //noinspection unchecked
            return (T) newClass.newInstance();
        } catch (Throwable e) {
            e.printStackTrace();

            return null;
        }
    }

    private static Class<?> createClass(Class<?> clazz) {
        String clsName = String.format("%sJavassistImpl", clazz.getSimpleName());
        String clsPath = String.format("%s.%s", clazz.getPackage(), clsName);

        ClassPool pool = JavassistClassPool.getClassPool(clazz);
        Class<?> myCls = JavassistClassPool.loadClass(pool, clsPath);
        if (myCls != null) {
            return myCls;
        }

        try {
            CtClass oldClass = pool.makeClass(clazz.getName());
            CtClass ctClass = pool.makeClass(clsPath);

            //添加继承类
            ctClass.setSuperclass(oldClass);

            //添加构造函数
            ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));

            //添加方法
            addMethod(clazz, ctClass);

            return ctClass.toClass();
        } catch (CannotCompileException e) {
            e.printStackTrace();

            return null;
        }
    }

    private static void addMethod(Class<?> clazz, CtClass ctClass) throws CannotCompileException {
        for (Method method : clazz.getDeclaredMethods()) {
            MethodAnnotation methodAnnotation = method.getAnnotation(MethodAnnotation.class);
            if (methodAnnotation == null) {
                return;
            }

            //构建表达式
            CodeBuilder toBuilder = new CodeBuilder()
                    .addStatement("public $C $N() {", method.getReturnType(), method.getName())
                    .addStatement("return $S;}", String.format("javassist value=%s", methodAnnotation.value()));

            //添加方法
            ctClass.addMethod(CtNewMethod.make(toBuilder.build(), ctClass));
        }
    }
}
