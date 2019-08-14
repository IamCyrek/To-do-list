package com.example.model;

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
    private long id;

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
    private Date createdAt;

}
