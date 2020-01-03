package com.example.controller.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static com.example.configuration.Constants.DATE_TIME_FORMAT;

@Data
public class UserFilterRequest {

    private String name;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private Date startTime;

    @DateTimeFormat(pattern = DATE_TIME_FORMAT)
    private Date endTime;

}
