package com.test.task.entities.dtos;

import com.test.task.entities.Form;
import lombok.Data;

import java.util.List;

@Data
public class ClientDto {
    private Long id;

    private String name;

    private String shortName;

    private String address;

    private List<DepositDto> deposits;

    private Form form;
}
