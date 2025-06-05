package com.moro.tchat.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class VerificationException extends RuntimeException {

    private HttpStatus httpStatus;

    public VerificationException(String message, HttpStatus httpStatus) {
        super(message);

        this.httpStatus = httpStatus;
    }

}
