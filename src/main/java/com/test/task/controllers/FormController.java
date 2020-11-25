package com.test.task.controllers;

import com.test.task.entities.Form;
import com.test.task.entities.dtos.FormDto;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.exceptions.NullIdException;
import com.test.task.services.FormService;
import com.test.task.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/forms")
@RestController
@Api("Set of endpoints for CRUD operations for forms")
@Slf4j
public class FormController {
    private FormService formService;

    @Autowired
    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping
    @ApiOperation("Returns list of all banks")
    public List<FormDto> get(){
        log.info("get forms");
        return formService.findAll();
    }

    @PostMapping
    @ApiOperation("Creates a new form. If id != null, then it will be cleared")
    public ResponseEntity<?> addForm(@RequestBody @Validated Form form, BindingResult bindingResult){
        log.info("Saving new form");
        if(form.getId() == null)
            form.setId(null);
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.ALL_FIELDS_MUST_BE_FILLED);
       return new ResponseEntity<>(formService.saveOrUpdate(form), HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("Modifies an existing form")
    public ResponseEntity<?> updateForm(@RequestBody @Validated Form form, BindingResult bindingResult){
        log.info("Update form");
        if(form.getId() == null)
            throw new NullIdException();
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.ALL_FIELDS_MUST_BE_FILLED);
        return new ResponseEntity<>(formService.saveOrUpdate(form), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletes a form from the system. 404 if the form's identifier is not found.")
    public void delete(@PathVariable Long id){
        log.info("delete form with id: " + id);
        formService.deleteById(id);
    }
}
