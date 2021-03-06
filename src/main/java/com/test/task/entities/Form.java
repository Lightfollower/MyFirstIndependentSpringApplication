package com.test.task.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "forms")
@NoArgsConstructor
@Data
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_id")
    private Long id;

    @NotNull
    @Column(name = "name_fld")
    private String name;

    public Form(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
