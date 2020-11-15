package com.test.task.controllers;

import com.test.task.entities.Form;
import com.test.task.entities.dtos.FormDto;
import com.test.task.services.FormService;
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
    FormService formService;

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
       return new ResponseEntity<>(formService.saveOrUpdate(form), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateForm(@RequestBody @Validated Form form, BindingResult bindingResult){
        if(form.getId() == null)
            throw new RuntimeException("id can't be null");
        return new ResponseEntity<>(formService.saveOrUpdate(form), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        formService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
