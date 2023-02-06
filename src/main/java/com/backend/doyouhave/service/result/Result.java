package com.backend.doyouhave.service.result;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private boolean success;
    private HttpStatus code;
    private String msg;
}
