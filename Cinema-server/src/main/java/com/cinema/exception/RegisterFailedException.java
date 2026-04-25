package com.cinema.exception;

/**
 * 注册失败异常
 */
public class RegisterFailedException extends RuntimeException {
    public RegisterFailedException(String message) {
        super(message);
    }
}
