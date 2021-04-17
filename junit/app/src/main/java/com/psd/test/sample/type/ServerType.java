package com.psd.test.sample.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * CodeType
 *
 * @author Created by gold on 2021/4/16 16:57
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum ServerType {

    /**
     * 成功
     */
    SUCCESS(200),
    /**
     * 异常
     */
    FAILURE(500);

    private final int type;

}
