package com.example.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
public class UserShortDTO {

    private long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    @NotNull
    private Date createdAt;

}
