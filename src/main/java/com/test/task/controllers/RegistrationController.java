package com.test.task.controllers;

import com.test.task.entities.dtos.SystemUser;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.exceptions.NonExistentIdException;
import com.test.task.exceptions.NullIdException;
import com.test.task.services.UserService;
import com.test.task.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/register")
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

    @PostMapping
    public ResponseEntity<?> registerNewAdministrator(@RequestBody @Validated SystemUser systemUser, BindingResult bindingResult) {
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
    public ResponseEntity<?> updateAdministrator(@RequestBody @Validated SystemUser systemUser, BindingResult bindingResult) {
        if (systemUser.getId() == null)
            throw new NullIdException();
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
    public void deleteClientById(@PathVariable Long id) {
//        log.info("Deleting client with id " + id);
        userService.deleteById(id);
    }
}
