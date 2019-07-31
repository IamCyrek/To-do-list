package com.example.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Data
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 3, max = 50)
    private String content;

    @Column(nullable = false)
    private boolean isRemoved;

}
