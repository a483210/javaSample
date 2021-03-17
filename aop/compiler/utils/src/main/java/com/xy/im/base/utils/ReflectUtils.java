package com.xy.im.base.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 反射创建类
 *
 * @author Created by gold on 2018/3/13 21:06
 */
public final class ReflectUtils {
    private ReflectUtils() {
    }

    /**
     * 反射获取对象静态参数
     *
     * @param clazz 源类型
     * @param key   对象名称
     */
    public static Object getAccessibleObject(Class<?> clazz, String key) {
        try {
            Field field = clazz.getDeclaredField(key);

            field.setAccessible(true);
            return field.get(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 反射获取对象参数
     *
     * @param src 源对象
     * @param key 对象名称
     */
    public static Object getAccessibleObject(Object src, String key) {
        try {
            Field field = searchField(src.getClass(), key);
            if (field == null) {
                return null;
            }

            field.setAccessible(true);
            return field.get(src);
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * 反射修改对象静态参数
     *
     * @param clazz 源类型
     * @param key   对象名称
     * @param dst   值
     */
    public static void setAccessibleObject(Class<?> clazz, String key, Object dst) {
        try {
            Field field = searchField(clazz, key);
            if (field == null) {
                return;
            }

            field.setAccessible(true);
            field.set(null, dst);
        } catch (Exception ignore) {
        }
    }

    /**
     * 反射修改对象参数
     *
     * @param src 源对象
     * @param key 对象名称
     * @param dst 值
     */
    public static void setAccessibleObject(Object src, String key, Object dst) {
        try {
            Field field = searchField(src.getClass(), key);
            if (field == null) {
                return;
            }

            field.setAccessible(true);
            field.set(src, dst);
        } catch (Exception ignore) {
        }
    }

    private static Field searchField(Class<?> clazz, String key) {
        while (clazz != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();

                if (Objects.equals(name, key)) {
                    return field;
                }
            }

            clazz = clazz.getSuperclass();
        }

        return null;
    }

    /**
     * 反射调用对象方法
     *
     * @param clazz 源类型
     * @param key   方法名称
     * @param args  参数
     */
    public static Object callAccessibleMethod(Class<?> clazz, String key, Object... args) {
        return callAccessibleMethod(clazz, null, key, args);
    }

    /**
     * 反射调用对象方法
     *
     * @param object 源对象
     * @param key    方法名称
     * @param args   参数
     */
    public static Object callAccessibleMethod(Object object, String key, Object... args) {
        return callAccessibleMethod(object.getClass(), object, key, args);
    }

    private static Object callAccessibleMethod(Class<?> clazz, Object object, String key, Object... args) {
        try {
            Method method = findMethod(clazz, key, args);

            method.setAccessible(true);

            return method.invoke(object, args);
        } catch (Throwable throwable) {
            if (throwable instanceof InvocationTargetException) {
                throwable = throwable.getCause();
            }

            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }

            throw new IllegalArgumentException(throwable);
        }
    }

    private static Method findMethod(Class<?> clazz, String key, Object[] args) {
        Class<?>[] classList = new Class<?>[args.length];
        for (int i = 0, count = classList.length; i < count; i++) {
            Object arg = args[i];
            if (arg == null) {
                throw new IllegalArgumentException("arg[" + i + "] can not be null");
            }

            classList[i] = arg.getClass();
        }

        Method method = searchMethod(clazz, key, classList);
        if (method == null) {
            throw new IllegalArgumentException("not found method");
        }

        return method;
    }

    private static Method searchMethod(Class<?> clazz, String key, Class<?>[] paramTypes) {
        while (clazz != null && clazz != Object.class) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                String name = method.getName();
                if (!Objects.equals(name, key)) {
                    continue;
                }

                if (!Arrays.deepEquals(method.getParameterTypes(), paramTypes)) {
                    continue;
                }

                return method;
            }

            clazz = clazz.getSuperclass();
        }

        return null;
    }

    /**
     * 获取对象所有类型对象
     *
     * @param clazz 类型
     */
    public static Map<String, Class<?>> getObjectClass(Class<?> clazz) {
        Map<String, Class<?>> map = new HashMap<>();

        while (clazz != null && !isSystemClass(clazz.getName())) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers)) {
                    continue;
                }

                String name = field.getName();
                map.put(name, field.getType());
            }

            clazz = clazz.getSuperclass();
        }

        return map;
    }

    /**
     * 解析当前索引泛型
     *
     * @param clazz 类
     * @param index 索引
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> parseType(Class<?> clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return null;
        }

        Type[] typeParams = ((ParameterizedType) genType).getActualTypeArguments();
        if (index < 0 || index >= typeParams.length) {
            return null;
        }

        return (Class<T>) typeParams[index];
    }

    /**
     * newInstance
     *
     * @param clazz class
     */
    public static <T> T newInstanceOfNullable(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ignore) {
            return null;
        }
    }

    /**
     * 转换原始类型为对象
     *
     * @param type 原始类型
     */
    public static Class<?> getType(Class<?> type) {
        if (type == Byte.TYPE) {
            return Byte.class;
        } else if (type == Short.TYPE) {
            return Short.class;
        } else if (type == Integer.TYPE) {
            return Integer.class;
        } else if (type == Long.TYPE) {
            return Long.class;
        } else if (type == Float.TYPE) {
            return Float.class;
        } else if (type == Double.TYPE) {
            return Double.class;
        } else if (type == Boolean.TYPE) {
            return Boolean.class;
        } else if (type == Character.TYPE) {
            return Character.class;
        } else {
            return type;
        }
    }

    /**
     * 判断是否为基础原始array，不包括对象
     *
     * @param type 类型
     */
    public static boolean isBasicArray(Type type) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            if (!clazz.isArray()) {
                return false;
            }

            return clazz.getComponentType().isPrimitive();
        }
        return false;
    }

    /**
     * 判断是否为基础原始类型，不包括对象
     *
     * @param type 类型
     */
    public static boolean isBasicPrimitive(Type type) {
        if (type instanceof Class<?>) {
            return ((Class<?>) type).isPrimitive();
        }
        return false;
    }

    /**
     * 判断是否为基础类型，如果不是class必定不是
     *
     * @param type 类型
     */
    public static boolean isPrimitive(Type type) {
        if (type instanceof Class<?>) {
            return isPrimitive(((Class<?>) type));
        }
        return false;
    }

    /**
     * 是否为基本类型
     *
     * @param clazz 类型
     */
    public static boolean isPrimitive(Class<?> clazz) {
        return clazz == String.class || formatType(clazz).isPrimitive();
    }

    /**
     * 判断是否为基础数组类型，如果不是class必定不是
     *
     * @param type 类型
     */
    public static boolean isPrimitiveArray(Type type) {
        if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            if (!clazz.isArray()) {
                return false;
            }

            return isPrimitive(clazz.getComponentType());
        }
        return false;
    }

    /**
     * 转换原始对象类型为基本类型
     *
     * @param type 原始对象类型
     */
    public static Class<?> formatType(Class<?> type) {
        if (type == Byte.class) {
            return Byte.TYPE;
        } else if (type == Short.class) {
            return Short.TYPE;
        } else if (type == Integer.class) {
            return Integer.TYPE;
        } else if (type == Long.class) {
            return Long.TYPE;
        } else if (type == Float.class) {
            return Float.TYPE;
        } else if (type == Double.class) {
            return Double.TYPE;
        } else if (type == Boolean.class) {
            return Boolean.TYPE;
        } else if (type == Character.class) {
            return Character.TYPE;
        } else {
            return type;
        }
    }

    /**
     * 是否为系统class
     *
     * @param name 类路径
     */
    public static boolean isSystemClass(String name) {
        return name.startsWith("java.") || name.startsWith("javax.")
                || name.startsWith("org.springframework.")
                || name.startsWith("com.smartfoxserver.");
    }

    /**
     * 方法是否存在
     *
     * @param cls  类型
     * @param name 密码名称
     */
    public static boolean existMethod(Class<?> cls, String name, Class<?>... parameterTypes) {
        try {
            cls.getMethod(name, parameterTypes);
            return true;
        } catch (NoSuchMethodException ignore) {
            return false;
        }
    }

    /**
     * 获取PropertyDescriptor
     *
     * @param clazz 类型
     * @param name  变量名称
     */
    public static PropertyDescriptor createPropertyDescriptor(Class<?> clazz, String name) {
        try {
            return new PropertyDescriptor(name, clazz);
        } catch (IntrospectionException ignore) {
            return null;
        }
    }

    /**
     * 获取set方法名称
     *
     * @param clazz 类型
     * @param name  变量名称
     */
    public static String setMethodName(Class<?> clazz, String name) {
        Method method = setMethod(clazz, name);
        if (method == null) {
            return null;
        }

        return method.getName();
    }

    /**
     * 获取get方法名称
     *
     * @param clazz 类型
     * @param name  变量名称
     */
    public static String getMethodName(Class<?> clazz, String name) {
        Method method = getMethod(clazz, name);
        if (method == null) {
            return null;
        }

        return method.getName();
    }

    /**
     * 获取set方法
     *
     * @param clazz 类型
     * @param name  变量名称
     */
    public static Method setMethod(Class<?> clazz, String name) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(name, clazz);

            return descriptor.getWriteMethod();
        } catch (IntrospectionException ignore) {
            return null;
        }
    }

    /**
     * 获取get方法
     *
     * @param clazz 类型
     * @param name  变量名称
     */
    public static Method getMethod(Class<?> clazz, String name) {
        try {
            PropertyDescriptor descriptor = new PropertyDescriptor(name, clazz);

            return descriptor.getReadMethod();
        } catch (IntrospectionException ignore) {
            return null;
        }
    }

    /**
     * 检查类型是否被支持
     *
     * @param type 类型
     */
    public static void checkType(Type type) {
        if (!(type instanceof Class
                || type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("the type must be class or parameterizedType");
        }
    }

    /**
     * 获得rawClass
     *
     * @param type type
     */
    public static Class<?> getRawClass(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof GenericArrayType) {
            return getRawClass(((GenericArrayType) type).getGenericComponentType());
        }

        return ((Class<?>) type);
    }

}
