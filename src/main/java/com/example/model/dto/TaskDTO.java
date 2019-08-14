package com.example.model.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class TaskDTO {

    private long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String content;

    @NotNull
    @Max(3)
    private int priority;

    @NotNull
    private Date creationTime;

    private boolean isRemoved;

    private long userId;

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

}
