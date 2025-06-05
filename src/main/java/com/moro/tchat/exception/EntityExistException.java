package com.moro.tchat.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
@Setter
public class EntityExistException extends RuntimeException {

    private HttpStatus httpStatus;

    private Map<String, String> errors;

    public EntityExistException(String message, HttpStatus httpStatus, Map<String, String> errors) {
        super(message);

        this.httpStatus = httpStatus;
        this.errors = errors;
    }

}
