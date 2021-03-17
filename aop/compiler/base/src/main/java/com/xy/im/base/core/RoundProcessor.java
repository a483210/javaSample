package com.xy.im.base.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;

/**
 * 处理信息
 *
 * @author Created by gold on 2019-09-02 15:33
 */
public class RoundProcessor {

    /**
     * 环境
     */
    private final RoundEnvironment roundEnv;
    /**
     * 注解
     */
    private final Set<? extends TypeElement> annotations;

    /**
     * 属性
     */
    private final Map<String, Object> attributes;
    /**
     * 参数
     */
    private final Map<String, List<Object>> params;

    public RoundProcessor(RoundEnvironment roundEnv,
                          Set<? extends TypeElement> annotations,
                          Map<String, Object> attributes,
                          Map<String, List<Object>> params) {
        this.roundEnv = roundEnv;
        this.annotations = annotations;
        this.attributes = attributes;
        this.params = params;
    }

    public RoundEnvironment getRoundEnv() {
        return roundEnv;
    }

    public Set<? extends TypeElement> getAnnotations() {
        return annotations;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public Map<String, List<Object>> getParams() {
        return params;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getParam(String paramsName) {
        List<Object> list = params.computeIfAbsent(paramsName, k -> new ArrayList<>());
        return (List<T>) list;
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public String getAttributeString(String name) {
        return String.valueOf(attributes.get(name));
    }

}
