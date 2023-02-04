package com.backend.doyouhave.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException", e);
        final ExceptionResponse response = ExceptionResponse.of(ExceptionCode.INVALID_INPUT_VALUE);
        return new ResponseEntity<>(response, ExceptionCode.INVALID_INPUT_VALUE.getStatus());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e) {
        log.error("NotFoundException", e);
        final ExceptionResponse response = ExceptionResponse.of(ExceptionCode.NOT_FOUND);
        return new ResponseEntity<>(response, ExceptionCode.NOT_FOUND.getStatus());
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ExceptionResponse> handleBusinessException(final BusinessException e) {
        log.error("BusinessException ", e);
        final ExceptionResponse response = ExceptionResponse.of(e.getErrorCode());
        return new ResponseEntity<>(response, e.getErrorCode().getStatus());
    }

    /**
     * 예상치 못한 에러를 잡기 위해 Exception handler 정의
     * return 500 Error
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.error("Exception", e);
        final ExceptionResponse response = ExceptionResponse.of(ExceptionCode.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(response, ExceptionCode.INTERNAL_SERVER_ERROR.getStatus());
    }
}
