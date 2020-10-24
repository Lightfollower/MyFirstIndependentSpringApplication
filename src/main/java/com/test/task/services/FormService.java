package com.test.task.services;

import com.test.task.entities.Form;
import com.test.task.entities.dtos.FormDto;
import com.test.task.repositories.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FormService {
    FormRepository formRepository;

    @Autowired
    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    public Form getByName(String name){
        return formRepository.getByName(name);
    }

    public FormDto getForm(Long id){
        return formRepository.getById(id);
    }



    public void addForm(Form form){
        System.out.println("ololo");
        System.out.println("ololo");
        System.out.println("ololo");
        System.out.println("ololo");
        System.out.println(form);
        System.out.println(form);
        System.out.println(form);
        System.out.println(form);
        System.out.println(form);
        formRepository.save(form);
    }
}
