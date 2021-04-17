package com.psd.test.sample.model.result;

import com.psd.test.sample.type.ServerType;
import lombok.Data;

/**
 * ServerResult
 *
 * @author Created by gold on 2021/4/16 16:56
 * @since 1.0.0
 */
@Data
public class ServerResult<T> {

    /**
     * 返回值
     */
    private T result;
    /**
     * 返回码
     *
     * @see ServerType
     */
    private int code;
    /**
     * 时间戳
     */
    private long timestamp;

    /**
     * 异常信息
     */
    private String errorMessage;

    public ServerResult() {
    }

    public ServerResult(T result) {
        this.result = result;
        this.code = ServerType.SUCCESS.getType();
        this.timestamp = System.currentTimeMillis();
    }

    public ServerResult(T result, int code, String errorMessage) {
        this.result = result;
        this.code = code;
        this.timestamp = System.currentTimeMillis();
        this.errorMessage = errorMessage;
    }

    /**
     * 成功返回
     *
     * @param result 返回值
     * @return result
     */
    public static <T> ServerResult<T> success(T result) {
        return new ServerResult<>(result);
    }

    /**
     * 异常返回
     *
     * @param throwable 异常
     * @return result
     */
    public static <T> ServerResult<T> failure(Throwable throwable) {
        return new ServerResult<>(null, ServerType.FAILURE.getType(), throwable.getMessage());
    }
}