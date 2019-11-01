package com.example.controller.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class TaskFilterRequest {

    private String content;

    private Integer priority;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date endTime;

    private Boolean isRemoved;

}
