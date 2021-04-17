package com.psd.test.sample.config;

import com.psd.test.sample.model.result.NullResult;
import com.psd.test.sample.model.result.ServerResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常配置类
 *
 * @author Created by gold on 2021/4/16 17:12
 * @since 1.0.0
 */
@Slf4j
@ControllerAdvice
public class ExceptionConfiguration {

    /**
     * 处理异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ServerResult<NullResult> handleException(Exception e) {
        return ServerResult.failure(e);
    }
}