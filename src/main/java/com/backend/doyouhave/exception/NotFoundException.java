package com.backend.doyouhave.exception;

public class NotFoundException extends BusinessException {

    public NotFoundException() {
        super(ExceptionCode.NOT_FOUND);
    }
}
