package com.example.controller.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.example.configuration.Constants.DATE_TIME_FORMAT;

@Data
public class UserFilterRequest {

    private String name;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime startTime;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime endTime;

}
