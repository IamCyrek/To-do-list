package com.example.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

import static com.example.configuration.Constants.DATE_TIME_FORMAT;

@Data
public class UserDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    @Email
    @NotBlank
    @Size(min = 3, max = 63)
    private String email;

    @NotBlank
    @Size(min = 8, max = 63)
    private String password;

    @NotNull
    @JsonFormat(pattern = DATE_TIME_FORMAT)
    private Date createdAt;

}
