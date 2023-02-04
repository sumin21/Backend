package com.backend.doyouhave.exception;

public class JwtException extends BusinessException {

    public JwtException() {
        super(ExceptionCode.JWT_EXCEPTION);
    }
}