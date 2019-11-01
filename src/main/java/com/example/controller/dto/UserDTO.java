package com.example.controller.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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

}
