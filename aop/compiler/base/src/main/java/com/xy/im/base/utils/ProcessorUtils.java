package com.xy.im.base.utils;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Symbol;
import com.xy.im.base.error.ProcessorException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleTypeVisitor8;
import javax.lang.model.util.Types;

/**
 * 核心工具类
 *
 * @author Created by gold on 2019-09-02 13:22
 */
public final class ProcessorUtils {
    private ProcessorUtils() {
    }

    private static ProcessingEnvironment processingEnv;
    private static Elements elements;
    private static Types types;

    public static TypeMirror TYPE_VOID,
            TYPE_STRING, TYPE_BOOLEAN,
            TYPE_BYTE, TYPE_SHORT,
            TYPE_INT, TYPE_LONG,
            TYPE_FLOAT, TYPE_DOUBLE,
            TYPE_COLLECTION, TYPE_LIST, TYPE_SET, TYPE_QUEUE, TYPE_MAP;

    public static void init(ProcessingEnvironment env) {
        processingEnv = env;

        elements = env.getElementUtils();
        types = env.getTypeUtils();

        initType();
    }

    private static void initType() {
        TYPE_VOID = toType(Void.class);
        TYPE_STRING = toType(String.class);
        TYPE_BOOLEAN = toType(Boolean.class);
        TYPE_BYTE = toType(Byte.class);
        TYPE_SHORT = toType(Short.class);
        TYPE_INT = toType(Integer.class);
        TYPE_LONG = toType(Long.class);
        TYPE_FLOAT = toType(Float.class);
        TYPE_DOUBLE = toType(Double.class);
        TYPE_COLLECTION = toDeclaredType(Collection.class);
        TYPE_LIST = toDeclaredType(List.class);
        TYPE_SET = toDeclaredType(Set.class);
        TYPE_QUEUE = toDeclaredType(Queue.class);
        TYPE_MAP = toDeclaredType(Map.class, 2);
    }

    /**
     * 检查是否为泛型继承类
     *
     * @param element       节点
     * @param cls           类型
     * @param annotationCls 注解类型
     */
    public static void checkSuperclassGeneric(Element element, Class<?> cls, Class<?> annotationCls) {
        checkPackage(element, annotationCls);

        TypeElement typeElement = ((TypeElement) element);
        TypeMirror generic = null;

        while (typeElement != null) {
            TypeMirror typeMirror = typeElement.getSuperclass();

            generic = getGenericType(typeMirror);
            if (generic != null) {
                break;
            }
            String typeMirrorName = toTypeName(typeMirror);
            if (Objects.equals(typeMirrorName, cls.getSimpleName())) {
                break;
            }

            typeElement = ((TypeElement) types.asElement(typeMirror));
        }

        if (generic == null) {
            throw new ProcessorException(element, "请正确使用泛型。");
        }

        TypeElement superTypeElement = toTypeElement(cls);

        DeclaredType type = types.getDeclaredType(superTypeElement, generic);

        if (!types.isSubtype(element.asType(), type)) {
            throw new ProcessorException(element, String.format("被%s注解的类必须继承于%s。",
                    annotationCls.getSimpleName(),
                    cls.getSimpleName()));
        }

    }

    /**
     * 检查是否为继承类
     *
     * @param element       节点
     * @param cls           类型
     * @param annotationCls 注解类型
     */
    public static void checkSuperclass(Element element, Class<?> cls, Class<?> annotationCls) {
        if (!types.isSubtype(((TypeElement) element).getSuperclass(), toType(cls))) {
            throw new ProcessorException(element, String.format("被%s注解的类必须继承于%s。",
                    annotationCls.getSimpleName(),
                    cls.getSimpleName()));
        }

        checkPackage(element, annotationCls);
    }

    /**
     * 检查是否为元素类
     *
     * @param element       节点
     * @param annotationCls 注解类型
     */
    public static void checkClassSymbol(Element element, Class<?> annotationCls) {
        checkPackage(element, annotationCls);

        if (!(element instanceof Symbol.ClassSymbol)) {
            throw new ProcessorException(element, "使用环境异常，无法解析为ClassSymbol！");
        }
    }

    /**
     * 检查是否为文件类
     *
     * @param element       节点
     * @param annotationCls 注解类型
     */
    public static void checkPackage(Element element, Class<?> annotationCls) {
        if (element.getEnclosingElement().getKind() != ElementKind.PACKAGE) {
            throw new ProcessorException(element, String.format("被%s注解的类必须是文件类。",
                    annotationCls.getSimpleName()));
        }
    }

    /**
     * 批量是否为文件类
     *
     * @param element       节点
     * @param annotationCls 注解类型
     */
    public static void checkPackage(Element element, Class<?>... annotationCls) {
        if (element.getEnclosingElement().getKind() != ElementKind.PACKAGE) {
            StringBuilder builder = new StringBuilder();

            for (int i = 0, count = annotationCls.length; i < count; i++) {
                Class<?> clazz = annotationCls[i];
                if (i > 0) {
                    builder.append("、");
                }
                builder.append(clazz.getSimpleName());
            }

            throw new ProcessorException(element, String.format("被%s注解的类必须是文件类。",
                    builder.toString()));
        }
    }

    /**
     * 是否为包
     *
     * @param element 节点
     */
    public static boolean isPackage(Element element) {
        return element.getEnclosingElement().getKind() == ElementKind.PACKAGE;
    }

    /**
     * 检查loader默认方法
     *
     * @param element 类
     */
    public static void checkMember(Element element) {
        List<String> sameMethods = Arrays.asList("init", "destroy");

        List<? extends Element> members = elements.getAllMembers(((TypeElement) element));
        for (Element member : members) {
            if (!(member instanceof ExecutableElement)) {
                continue;
            }
            String name = member.getSimpleName().toString();
            if (sameMethods.contains(name)) {
                throw new ProcessorException(member, "方法命名重复！");
            }
        }
    }

    /**
     * 判断类型是否相等
     *
     * @param typeMirror1 类型1
     * @param typeMirror2 类型2
     */
    public static boolean isSameType(TypeMirror typeMirror1, TypeMirror typeMirror2) {
        return types.isSameType(typeMirror1, typeMirror2);
    }

    /**
     * 是否为子类型
     *
     * @param typeMirror1 类型1
     * @param typeMirror2 类型2
     */
    public static boolean isSubtype(TypeMirror typeMirror1, TypeMirror typeMirror2) {
        return types.isSubtype(typeMirror1, typeMirror2);
    }

    /**
     * 是否可分配
     *
     * @param typeMirror1 类型1
     * @param typeMirror2 类型2
     */
    public static boolean isAssignable(TypeMirror typeMirror1, TypeMirror typeMirror2) {
        return types.isAssignable(typeMirror1, typeMirror2);
    }

    /**
     * 泛型转class
     *
     * @param genericType 泛型
     */
    public static Class<?> toTypeClass(TypeMirror genericType) {
        if (isSameType(genericType, TYPE_BOOLEAN)) {
            return Boolean.class;
        } else if (isSameType(genericType, TYPE_BYTE)) {
            return Byte.class;
        } else if (isSameType(genericType, TYPE_SHORT)) {
            return Short.class;
        } else if (isSameType(genericType, TYPE_INT)) {
            return Integer.class;
        } else if (isSameType(genericType, TYPE_LONG)) {
            return Long.class;
        } else if (isSameType(genericType, TYPE_FLOAT)) {
            return Float.class;
        } else if (isSameType(genericType, TYPE_DOUBLE)) {
            return Double.class;
        } else if (isSameType(genericType, TYPE_STRING)) {
            return String.class;
        }

        return null;
    }

    public static TypeName getMapGenericTypeName(DeclaredType declaredType) {
        TypeElement declaredElement = (TypeElement) declaredType.asElement();
        DeclaredType declaredGenericType = toDeclaredType(declaredElement, 2);

        TypeName returnClassName;
        if (isSameType(declaredGenericType, TYPE_MAP)) {
            returnClassName = TypeName.get(HashMap.class);
        } else {
            returnClassName = ClassName.get(declaredElement);
        }
        return returnClassName;
    }

    public static TypeName getCollectGenericTypeName(DeclaredType declaredType) {
        TypeElement declaredElement = (TypeElement) declaredType.asElement();
        DeclaredType declaredGenericType = toDeclaredType(declaredElement);

        TypeName returnClassName;
        if (isSameType(declaredGenericType, TYPE_LIST)) {
            returnClassName = TypeName.get(ArrayList.class);
        } else if (isSameType(declaredGenericType, TYPE_SET)) {
            returnClassName = TypeName.get(HashSet.class);
        } else if (isSameType(declaredGenericType, TYPE_QUEUE)) {
            returnClassName = TypeName.get(LinkedList.class);
        } else {
            returnClassName = ClassName.get(declaredElement);
        }

        return returnClassName;
    }

    /**
     * 判断变量是否为public
     *
     * @param element 节点
     */
    public static boolean isFiledPublic(VariableElement element) {
        return element.getModifiers().contains(Modifier.PUBLIC);
    }

    /**
     * 判断变量是否为transient
     *
     * @param element 节点
     */
    public static boolean isFiledTransient(VariableElement element) {
        return element.getModifiers().contains(Modifier.TRANSIENT)
                || element.getModifiers().contains(Modifier.STATIC);
    }

    /**
     * 判断变量是否为final
     *
     * @param element 节点
     */
    public static boolean isFiledFinal(VariableElement element) {
        return element.getModifiers().contains(Modifier.FINAL);
    }

    public static Class<?> toClass(TypeKind typeKind) {
        switch (typeKind) {
            case BOOLEAN:
                return Boolean.class;
            case BYTE:
                return Byte.class;
            case SHORT:
                return Short.class;
            case INT:
                return Integer.class;
            case LONG:
                return Long.class;
            case FLOAT:
                return Float.class;
            case DOUBLE:
            default:
                return Double.class;
        }
    }

    public static String toTypeName(TypeKind typeKind) {
        switch (typeKind) {
            case BOOLEAN:
                return "Boolean";
            case BYTE:
                return "Byte";
            case SHORT:
                return "Short";
            case INT:
                return "Integer";
            case LONG:
                return "Long";
            case FLOAT:
                return "Float";
            case DOUBLE:
            default:
                return "Double";
        }
    }

    /**
     * 获取泛型
     *
     * @param element 类
     */
    public static TypeMirror getGenericElement(Element element) {
        if (!(element instanceof TypeElement)) {
            return null;
        }

        TypeElement typeElement = (TypeElement) element;
        TypeMirror generic = null;

        while (true) {
            TypeMirror superclass = typeElement.getSuperclass();
            if (superclass instanceof DeclaredType) {
                DeclaredType declaredType = (DeclaredType) superclass;

                List<? extends TypeMirror> typeMirrors = declaredType.getTypeArguments();
                if (!TextUtils.isEmpty(typeMirrors)) {
                    generic = typeMirrors.get(0);
                    break;
                }
            }

            Element superElement = types.asElement(superclass);
            if (superElement == null) {
                break;
            }

            String claName = superElement.toString();
            if (claName.startsWith("java.") || claName.startsWith("javax.")) {
                break;
            }
            if (!(superElement instanceof TypeElement)) {
                break;
            }

            typeElement = (TypeElement) superElement;
        }

        if (generic == null) {
            return null;
        }

        return generic;
    }

    public static String toTypeName(DeclaredType declaredType) {
        List<? extends TypeMirror> args = declaredType.getTypeArguments();
        if (TextUtils.isEmpty(args)) {
            return null;
        }
        return toTypeName(args.get(0));
    }

    public static String toTypeName(TypeMirror typeMirror) {
        return types.asElement(typeMirror).getSimpleName().toString();
    }

    public static String toLowerClassName(String name) {
        if ("Integer".equals(name)) {
            return "int";
        }
        return name.toLowerCase();
    }

    /**
     * 将class转换为typeMirror
     *
     * @param cls class
     */
    public static TypeMirror toType(Class<?> cls) {
        return toTypeElement(cls).asType();
    }

    /**
     * 动态返回type
     *
     * @param element element
     */
    public static TypeMirror toDynamicType(Element element) {
        TypeMirror typeMirror = element.asType();
        if (!(element instanceof TypeElement)) {
            return typeMirror;
        } else if (!(typeMirror instanceof DeclaredType)) {
            return typeMirror;
        }

        TypeKind typeKind = typeMirror.getKind();
        if (typeKind != TypeKind.DECLARED) {
            return typeMirror;
        }

        DeclaredType declaredType = ((DeclaredType) typeMirror);
        List<? extends TypeMirror> typeMirrors = declaredType.getTypeArguments();
        if (TextUtils.isEmpty(typeMirrors)) {
            return typeMirror;
        }

        return toDeclaredType((TypeElement) element, typeMirrors.size());
    }

    /**
     * 将单个泛型class转换为typeMirror <?>
     *
     * @param cls class
     */
    public static DeclaredType toDeclaredType(Class<?> cls) {
        return toDeclaredType(cls, 1);
    }

    /**
     * 将泛型class转换为typeMirror <?>
     *
     * @param cls class
     */
    public static DeclaredType toDeclaredType(Class<?> cls, int genericNumber) {
        TypeMirror[] genericTypes = new TypeMirror[genericNumber];
        for (int i = 0; i < genericNumber; i++) {
            genericTypes[i] = types.getWildcardType(null, null);
        }

        return types.getDeclaredType(toTypeElement(cls), genericTypes);
    }

    /**
     * 将单个泛型element转换为typeMirror <?>
     *
     * @param element element
     */
    public static DeclaredType toDeclaredType(TypeElement element) {
        return toDeclaredType(element, 1);
    }

    /**
     * 将泛型element转换为typeMirror <?>
     *
     * @param element element
     */
    public static DeclaredType toDeclaredType(TypeElement element, int genericNumber) {
        TypeMirror[] genericTypes = new TypeMirror[genericNumber];
        for (int i = 0; i < genericNumber; i++) {
            genericTypes[i] = types.getWildcardType(null, null);
        }

        return types.getDeclaredType(element, genericTypes);
    }

    /**
     * 将class转换为element
     *
     * @param cls class
     */
    public static TypeElement toTypeElement(Class<?> cls) {
        return elements.getTypeElement(cls.getCanonicalName());
    }

    /**
     * element转换为类名
     *
     * @param element 类
     */
    public static String toTypeName(Element element) {
        String path = element.getEnclosingElement().toString();
        String clsName = element.getSimpleName().toString();

        return String.format("%s.%s", path, clsName);
    }

    /**
     * 插入路径
     *
     * @param element  节点
     * @param rootPath 根路径
     * @param name     插入名称
     */
    public static String toDirPath(Element element, String rootPath, String name) {
        String path = element.getEnclosingElement().toString();

        String relPath = path.replace(rootPath, "");
        if (path.equals(relPath)) {
            throw new ProcessorException(element, String.format("%s超过根路径%s。", path, rootPath));
        }

        return String.format("%s.%s%s", rootPath, name, relPath);
    }

    /**
     * 获取注解里class参数
     *
     * @param element 类
     * @param cls     注解类
     */
    public static DeclaredType getAnnotationClass(Element element, Class<? extends Annotation> cls) {
        TypeMirror typeMirror = toType(cls);

        return element.getAnnotationMirrors()
                .stream()
                .filter(mirror -> mirror.getAnnotationType() == typeMirror)
                .map(AnnotationMirror::getElementValues)
                .map(Map::values)
                .flatMap(Collection::stream)
                .filter(value -> value instanceof Attribute.Class)
                .map(AnnotationValue::getValue)
                .filter(value -> value instanceof DeclaredType)
                .map(value -> (DeclaredType) value)
                .findFirst()
                .orElse(null);
    }

    /**
     * 创建类
     *
     * @param path     路径
     * @param typeSpec 类构件
     */
    public static void createClass(String path, TypeSpec typeSpec) {
        try {
            JavaFile.builder(path, typeSpec)
                    .build()
                    .writeTo(processingEnv.getFiler());
        } catch (IOException ignored) {
        }
    }

    /**
     * 获取泛型，旧方法
     *
     * @param type 类型
     */
    public static TypeMirror getGenericType(TypeMirror type) {
        TypeMirror[] result = {null};

        type.accept(new SimpleTypeVisitor8<Void, Void>() {

            @Override
            public Void visitDeclared(DeclaredType declaredType, Void v) {
                List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
                if (!typeArguments.isEmpty()) {
                    result[0] = typeArguments.get(0);
                }
                return null;
            }

            @Override
            protected Void defaultAction(TypeMirror typeMirror, Void v) {
                throw new UnsupportedOperationException();
            }

        }, null);

        return result[0];
    }

}
