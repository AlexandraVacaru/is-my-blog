package com.unibuc.ismyblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorException extends RuntimeException{

    public InternalErrorException() {
    }
    public InternalErrorException(String message) {
        super(message);
    }
    public InternalErrorException(String message, Throwable
            throwable) {
        super(message, throwable);
    }
}