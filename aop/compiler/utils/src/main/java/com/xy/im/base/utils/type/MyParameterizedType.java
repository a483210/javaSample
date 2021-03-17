package com.xy.im.base.utils.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * 自定义ParameterizedType
 *
 * @author Created by gold on 2020/5/18 15:19
 */
public class MyParameterizedType implements ParameterizedType {

    private final Type[] types;
    private final Type rawType;

    public MyParameterizedType(Type[] types, Type rawType) {
        this.types = types;
        this.rawType = rawType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return types.clone();
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return null;
    }

    @Override
    public String toString() {
        return toParameterizedTypeString(this);
    }

    public static String toParameterizedTypeString(MyParameterizedType parameterizedType) {
        Type[] types = parameterizedType.getActualTypeArguments();
        Type rawType = parameterizedType.getRawType();

        StringBuilder builder = new StringBuilder();
        builder.append("rawType ")
                .append(rawType.getTypeName());

        if (types == null || types.length == 0) {
            return builder.toString();
        }

        builder.append(" [types ");
        for (Type type : types) {
            builder.append(type.getTypeName());
        }
        builder.append("]");

        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ParameterizedType)) {
            return false;
        }
        ParameterizedType otherType = (ParameterizedType) other;
        return (otherType.getOwnerType() == null && this.rawType.equals(otherType.getRawType()) &&
                Arrays.equals(this.types, otherType.getActualTypeArguments()));
    }

    @Override
    public int hashCode() {
        return (this.rawType.hashCode() * 31 + Arrays.hashCode(this.types));
    }

}