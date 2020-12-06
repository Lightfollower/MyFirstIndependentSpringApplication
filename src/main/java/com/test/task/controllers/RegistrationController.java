package com.test.task.controllers;

import com.test.task.entities.dtos.SystemUser;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.exceptions.NonExistentIdException;
import com.test.task.exceptions.NullIdException;
import com.test.task.services.UserService;
import com.test.task.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/register")
@Api("Set of endpoints for CRUD operations for users")
@Slf4j
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

    @GetMapping
    @ApiOperation("Returns list of all users with empty password fields")
    public List<SystemUser> getUsers(){
        log.info("Get users");
        return userService.findAll();
    }

    @PostMapping
    @ApiOperation("Creates a new user with role admin. If id != null, then it will be cleared")
    public ResponseEntity<?> registerNewAdministrator(@RequestBody @Validated SystemUser systemUser, BindingResult bindingResult) {
        log.info("Saving new user");
        if (systemUser.getName() != null)
            systemUser.setId(null);
        if (bindingResult.hasFieldErrors("name"))
            throw new MalformedEntityException("Field name must be filled");
        if (bindingResult.hasErrors())
            throw new MalformedEntityException("Passwords must match and be at least 3 characters");
        userService.saveOrUpdate(systemUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @ApiOperation("Modifies an existing user")
    public ResponseEntity<?> updateAdministrator(@RequestBody @Validated SystemUser systemUser, BindingResult bindingResult) {
        if (systemUser.getId() == null)
            throw new NullIdException();
        log.info("Updating user with id " + systemUser.getId());
        if (systemUser.getName() == null)
            throw new MalformedEntityException("Field name must be filled");
        if (!userService.existById(systemUser.getId()))
            throw new NonExistentIdException(String.format(Constants.NO_OBJECT_WITH_THIS_ID, Constants.USER_STRING));
        if (bindingResult.hasErrors())
            throw new MalformedEntityException("Passwords must match and be at least 3 characters");
        userService.saveOrUpdate(systemUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletes a user from the system. 404 if the users's identifier is not found.")
    public void deleteClientById(@PathVariable Long id) {
        log.info("Deleting user with id " + id);
        userService.deleteById(id);
    }
}
