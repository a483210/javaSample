package com.xy.im.base.utils.type;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

/**
 * MyGenericArrayType
 *
 * @author Created by gold on 2020/5/20 10:46
 */
public class MyGenericArrayType implements GenericArrayType {

    private final Type componentType;

    public MyGenericArrayType(Type componentType) {
        this.componentType = componentType;
    }

    @Override
    public Type getGenericComponentType() {
        return componentType;
    }

    public String toString() {
        Type componentType = this.getGenericComponentType();
        StringBuilder sb = new StringBuilder();
        if (componentType instanceof Class) {
            sb.append(((Class<?>) componentType).getName());
        } else {
            sb.append(componentType.toString());
        }

        sb.append("[]");
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof GenericArrayType) {
            GenericArrayType that = (GenericArrayType) o;
            Type thatComponentType = that.getGenericComponentType();
            return this.componentType.equals(thatComponentType);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.componentType.hashCode();
    }

}
