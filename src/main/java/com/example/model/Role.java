package com.example.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@Table
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Size(max = 50)
    private RoleName name;

}
