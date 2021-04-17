package com.psd.test.sample.model.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * DeployLayer
 *
 * @author Created by gold on 2021/4/16 14:45
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class DeployLayer {

    /**
     * 层序列
     */
    private final Integer layer;
    /**
     * 部署数据
     */
    private final List<DeployBean> deploys;

}
