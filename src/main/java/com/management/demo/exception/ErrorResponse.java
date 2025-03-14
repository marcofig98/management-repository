package com.management.demo.exception;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String path;

    public ErrorResponse(int status, String error, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.path = path;
    }

}

