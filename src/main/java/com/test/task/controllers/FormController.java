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
    public void add(@RequestBody Form form){
        formService.addForm(new Form(form.getId(), form.getName()));
    }
}
