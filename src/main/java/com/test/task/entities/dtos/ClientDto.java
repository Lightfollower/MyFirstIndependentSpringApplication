package com.test.task.entities.dtos;

import com.test.task.entities.Form;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "Client dto in the application.")
public class ClientDto {
    @ApiModelProperty(notes = "Unique identifier of the client. No two clients can have the same id.", example = "1", required = true, position = 0)
    private Long id;

    @ApiModelProperty(notes = "Name of the client. No two clients can have the same name.", example = "Vasya", required = true, position = 1)
    private String name;

    @ApiModelProperty(notes = "Short name of the client.", example = "Vs", required = true, position = 2)
    private String shortName;

    @ApiModelProperty(notes = "Address of the client", example = "Hell", required = true, position = 3)
    private String address;

    @ApiModelProperty(notes = "Organization form of the client, selected from the table of forms, form id is enough", example = "{id = 1}", required = true, position = 4)
    private FormDto form;
}
