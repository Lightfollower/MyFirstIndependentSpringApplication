package com.test.task.entities.dtos;

import com.test.task.utils.validation.FieldMatch;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match")
public class SystemUser {
    @NotNull(message = "Name is required")
    private String name;

    @NotNull(message = "is required")
    @Size(min = 3, message = "password is too short")
    private String password;

    @NotNull(message = "is required")
    @Size(min = 3, message = "password is too short")
    private String matchingPassword;
}
