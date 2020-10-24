package com.test.task.entities.dtos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import java.sql.Date;

@Data
public class DepositDto {
    private Long id;

    private Date date;

    private Double rate;

    private int term;

    @JsonBackReference
    private String client;

    @JsonBackReference
    private String bank;
}
