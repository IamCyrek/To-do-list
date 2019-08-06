package com.example.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    @NotBlank
    @Size(min = 3, max = 255)
    private String content;

    @Column(nullable = false)
    @NotNull
    @Max(3)
    private int priority;

    @Column(nullable = false)
    @NotNull
    private Date creationTime;

    @Column(nullable = false)
    private boolean isRemoved;

}
