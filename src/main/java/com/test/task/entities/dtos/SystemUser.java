package com.test.task.entities.dtos;

import com.test.task.utils.validation.FieldMatch;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match")
@ApiModel(description = "User dto in the application.")
public class SystemUser {
    @ApiModelProperty(notes = "Unique identifier of the user. No two users can have the same id.", example = "1", required = true, position = 0)
    private Long id;

    @NotNull(message = "Name is required")
    @ApiModelProperty(notes = "Name of the user. No two users can have the same name.", example = "Vasya", required = true, position = 1)
    private String name;

    @NotNull(message = "is required")
    @Size(min = 3, message = "password is too short")
    @ApiModelProperty(notes = "password for authorization")
    private String password;

    @NotNull(message = "is required")
    @Size(min = 3, message = "password is too short")
    @ApiModelProperty(notes = "password check")
    private String matchingPassword;

    private ArrayList<String> roles;
}
