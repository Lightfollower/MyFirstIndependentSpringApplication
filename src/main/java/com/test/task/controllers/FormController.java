package com.test.task.controllers;

import com.test.task.entities.Form;
import com.test.task.entities.dtos.FormDto;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.exceptions.NullIdException;
import com.test.task.exceptions.nonExistentIdException;
import com.test.task.services.FormService;
import com.test.task.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/forms")
@RestController
public class FormController {
    private FormService formService;

    @Autowired
    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping
    public List<FormDto> get(){
        return formService.findAll();
    }

    @PostMapping
    public ResponseEntity<?> addForm(@RequestBody @Validated Form form, BindingResult bindingResult){
        if(form.getId() == null)
            form.setId(null);
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.allFieldsMustBeFilled);
       return new ResponseEntity<>(formService.saveOrUpdate(form), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateForm(@RequestBody @Validated Form form, BindingResult bindingResult){
        if(form.getId() == null)
            throw new NullIdException();
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.allFieldsMustBeFilled);
        return new ResponseEntity<>(formService.saveOrUpdate(form), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        if (!formService.existsById(id))
            throw new nonExistentIdException(Constants.noObjectWithThisId);
        formService.deleteById(id);
    }
}
