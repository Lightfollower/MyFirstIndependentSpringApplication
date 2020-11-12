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
            formDtos.add(getDtoFromForm(f));
        }
        return formDtos;
    }

    private FormDto getDtoFromForm(Form f) {
        FormDto formDto = new FormDto();
        formDto.setId(f.getId());
        formDto.setName(f.getName());
        return formDto;
    }

    public Form getByName(String name){
        return formRepository.getByName(name);
    }

    public FormDto getFormById(Long id){
        return formRepository.getById(id);
    }

    public Form getOne(Long id) {
        return formRepository.getOne(id);
    }



    public FormDto saveOrUpdate(Form form){
        return getFormDtoFromForm(formRepository.save(form));
    }

    public void deleteById(Long id){
         formRepository.deleteById(id);
    }

//    public List<Form> findAll() {
//        return formRepository.findAll();
//    }

    public FormDto getFormDtoFromForm(Form form) {
        FormDto formDto = new FormDto();
        formDto.setId(form.getId());
        formDto.setName(form.getName());
        return formDto;
    }
}
