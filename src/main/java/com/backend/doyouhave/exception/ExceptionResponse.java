package com.backend.doyouhave.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExceptionResponse {
    private String message;
    private int status;

    private ExceptionResponse(final ExceptionCode code) {
        this.message = code.getMessage();
        this.status = code.getStatus().value();
    }

    private ExceptionResponse(final ExceptionCode code, final String message) {
        this.status = code.getStatus().value();
        this.message = message;
    }

    public static ExceptionResponse of(final ExceptionCode code) {
        return new ExceptionResponse(code);
    }

    public static ExceptionResponse of(final ExceptionCode code, final String additionalMessage) {
        String message = String.format("%s - %s", code.getMessage(), additionalMessage);
        return new ExceptionResponse(code, message);
    }

}
