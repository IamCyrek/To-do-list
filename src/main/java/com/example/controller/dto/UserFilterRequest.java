package com.example.controller.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class UserFilterRequest {

    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date endTime;

}
