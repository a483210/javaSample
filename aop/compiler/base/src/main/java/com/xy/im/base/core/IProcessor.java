package com.xy.im.base.core;

/**
 * 处理
 *
 * @author Created by gold on 2019-08-30 17:52
 */
public interface IProcessor {

    String registerAnnotationType();

    void process(RoundProcessor roundEnv);

}
