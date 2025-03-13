package com.management.demo.user.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UserConflictException extends RuntimeException {

    public UserConflictException(String email) {
        super("User already exists with that email: " + email);
    }
}
