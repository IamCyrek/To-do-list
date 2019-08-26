package com.example.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Data
@Table(schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    @Column(nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(min = 3, max = 63)
    private String email;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 8, max = 63)
    private String password;

    @Column(name = "created_at", nullable = false)
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private Date createdAt;

}
