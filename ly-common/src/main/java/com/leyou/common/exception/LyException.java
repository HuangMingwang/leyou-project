package com.leyou.common.exception;

import lombok.Getter;

/**
 * @author Huang Mingwang
 * @create 2021-05-20 1:58 下午
 */
@Getter
public class LyException extends RuntimeException {
    private int status;

    public LyException(int status) {
        this.status = status;
    }

    public LyException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public LyException(int status, String message) {
        super(message);
        this.status = status;
    }

    public LyException(int status, Throwable cause) {
        super(cause);
        this.status = status;
    }
}
