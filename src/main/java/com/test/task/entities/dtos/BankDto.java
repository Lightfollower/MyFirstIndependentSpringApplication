package com.test.task.entities.dtos;

import java.util.List;

public interface BankDto {
    Long getId();

    String getName();

    String getBIC();

    List<DepositDto> getDeposits();
}
