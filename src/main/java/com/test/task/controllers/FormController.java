package com.test.task.controllers;

import com.test.task.entities.Form;
import com.test.task.entities.dtos.FormDto;
import com.test.task.services.FormService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public FormDto addForm(@RequestBody Form form){
        if(form.getId() == null)
            form.setId(null);
       return formService.saveOrUpdate(form);
    }

    @PutMapping
    public FormDto updateForm(@RequestBody Form form){
        if(form.getId() == null)
            throw new RuntimeException("id can't be null");
        return formService.saveOrUpdate(form);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        formService.deleteById(id);
    }
}
