package com.test.task.entities.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Date;

@Data
@ApiModel(description = "Deposit dto in the application.")
public class DepositDto {
    @ApiModelProperty(notes = "Unique identifier of the deposit. No two deposits can have the same id.", example = "1", required = true, position = 0)
    private Long id;

    @ApiModelProperty(notes = "deposit opening date", example = "2020-12-20", required = true, position = 1)
    private Date date;

    @ApiModelProperty(notes = "Rate of the deposit.", example = "3.2", required = true, position = 2)
    private Double rate;

    @ApiModelProperty(notes = "Term in months for which the deposit is made", example = "15", required = true, position = 3)
    private int term;

    @ApiModelProperty(notes = "Client who opened a deposit, client id is enough", example = "{id = 1}", required = true, position = 4)
    private ClientDto client;

    @ApiModelProperty(notes = "Bank in which the deposit is open, bank id is enough", example = "{id = 1}", required = true, position = 5)
    private BankDto bank;
}
