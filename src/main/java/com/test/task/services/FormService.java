package com.test.task.services;

import com.test.task.entities.Form;
import com.test.task.entities.dtos.FormDto;
import com.test.task.repositories.FormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FormService {
    FormRepository formRepository;

    @Autowired
    public FormService(FormRepository formRepository) {
        this.formRepository = formRepository;
    }

    public List<FormDto> findAll(){
        List<FormDto> formDtos = new ArrayList<>();
        List<Form> forms = formRepository.findAll();
        for (Form f :
                forms) {
            formDtos.add(getFormDtoFromForm(f));
        }
        return formDtos;
    }

    public Form getByName(String name){
        return formRepository.getByName(name);
    }

    public FormDto saveOrUpdate(Form form){
        return getFormDtoFromForm(formRepository.save(form));
    }

    public boolean existsById(Long id) {
        return formRepository.existsById(id);
    }

    public void deleteById(Long id){
         formRepository.deleteById(id);
    }

    FormDto getFormDtoFromForm(Form form) {
        FormDto formDto = new FormDto();
        formDto.setId(form.getId());
        formDto.setName(form.getName());
        return formDto;
    }
}
