package com.example.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
class ErrorDetails {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;

}
