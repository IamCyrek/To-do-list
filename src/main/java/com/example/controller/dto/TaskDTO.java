package com.example.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.example.configuration.Constants.DATE_TIME_FORMAT;

@Data
public class TaskDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String content;

    @NotNull
    @Max(3)
    private Integer priority;

    @NotNull
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private LocalDateTime creationTime;

    private Boolean isRemoved;

    private Long userId;

}
