package com.test.task.controllers;

import com.test.task.entities.User;
import com.test.task.entities.dtos.SystemUser;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class RegistrationController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @PostMapping("/register")
    public ResponseEntity<?> processRegistrationForm(@RequestBody @Validated SystemUser systemUser, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors("name"))
            throw new MalformedEntityException("Field name must be filled");
        if (bindingResult.hasErrors())
            throw new MalformedEntityException("Passwords must match and be at least 3 characters");
//        Optional<User> existing = userService.findByName(systemUser.getName());
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
