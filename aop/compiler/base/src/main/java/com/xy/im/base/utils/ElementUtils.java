package com.xy.im.base.utils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.List;

/**
 * ElementUtils
 *
 * @author Created by gold on 2021/3/14 18:40
 */
public class ElementUtils {
    private ElementUtils() {
    }

    /**
     * 查找变量节点
     *
     * @param elements 所有节点成员
     * @return list
     */
    public static List<VariableElement> searchFiled(List<? extends Element> elements) {
        List<VariableElement> es = new ArrayList<>(elements.size());

        for (Element element : elements) {
            if (!(element instanceof VariableElement)) {
                continue;
            }

            es.add(((VariableElement) element));
        }

        return es;
    }

    /**
     * 查找方法节点
     *
     * @param elements 所有节点成员
     * @return list
     */
    public static List<ExecutableElement> searchMethod(List<? extends Element> elements) {
        List<ExecutableElement> es = new ArrayList<>(elements.size());

        for (Element element : elements) {
            if (!(element instanceof ExecutableElement)) {
                continue;
            }

            es.add(((ExecutableElement) element));
        }

        return es;
    }
}
