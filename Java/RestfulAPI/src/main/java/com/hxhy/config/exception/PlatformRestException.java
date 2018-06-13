package com.hxhy.config.exception;

import org.springframework.http.HttpStatus;

/**
 * 专用于Restful Service的异常.
 * 
 */
public class PlatformRestException extends RuntimeException {

    private static final long serialVersionUID = 3760831275979742006L;

    public HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    public PlatformRestException() {
    }

    public PlatformRestException(HttpStatus status) {
        this.status = status;
    }

    public PlatformRestException(String message) {
        super(message);
    }

    public PlatformRestException(HttpStatus status, String message) {
        super("{\"message\":接口异常：" + message + ",\"status\":-3}");
        this.status = status;
    }

    public PlatformRestException(HttpStatus status, String message, int flag) {
        super("{\"message\":接口异常：" + message + ",\"status\":" + flag + "}");
        this.status = status;
    }
}
