package com.test.task.entities.dtos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Form dto in the application.")
public class FormDto {
    @ApiModelProperty(notes = "Unique identifier of the deposit. No two deposits can have the same id.", example = "1", required = true, position = 0)
    private Long id;

    @ApiModelProperty(notes = "Name of the form.", example = "LLC", required = true, position = 1)
    private String name;
}
