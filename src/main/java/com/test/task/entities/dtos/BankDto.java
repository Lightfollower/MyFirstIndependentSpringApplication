package com.test.task.entities.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "Bank dto in the application.")
public class BankDto {
    @ApiModelProperty(notes = "Unique identifier of the bank. No two banks can have the same id.", example = "1", required = true, position = 0)
    private Long id;

    @ApiModelProperty(notes = "Name of the bank. No two banks can have the same name", example = "Horns and Hooves", required = true, position = 1)
    private String name;

    @ApiModelProperty(notes = "BIC of the bank. Can't contain non numeric symbols", example = "123456789", required = true, position = 2)
    private String BIC;

//    private List<DepositDto> deposits;
}
