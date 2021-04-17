package com.psd.test.sample.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DeployBean
 *
 * @author Created by gold on 2021/4/16 14:39
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class DeployBean {

    /**
     * 部署名称
     */
    private final String name;
    /**
     * 部署镜像
     */
    private final String value;
    /**
     * 依赖于
     */
    private final List<String> depends;

    public DeployBean(String name, String value) {
        this.name = name;
        this.value = value;
        this.depends = null;
    }
}