package com.test.task.entities.dtos;

import lombok.Data;

import java.util.List;

@Data
public class BankDto {
    private Long id;

    private String name;

    private String BIC;

//    private List<DepositDto> deposits;
}
