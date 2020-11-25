package com.test.task.services;

import com.test.task.entities.Form;
import com.test.task.entities.dtos.FormDto;
import com.test.task.exceptions.NonExistentIdException;
import com.test.task.repositories.FormRepository;
import com.test.task.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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

    public List<FormDto> findAll() {
        List<FormDto> formDtos = new ArrayList<>();
        List<Form> forms = formRepository.findAll();
        for (Form f :
                forms) {
            formDtos.add(getFormDtoFromForm(f));
        }
        return formDtos;
    }

    public Form getByName(String name) {
        if (!formRepository.existsByName(name))
            throw new ResourceNotFoundException(String.format(Constants.NO_OBJECT_WITH_THIS_NAME, "form"));
        return formRepository.getByName(name);
    }

    public FormDto saveOrUpdate(Form form) {
        return getFormDtoFromForm(formRepository.save(form));
    }

    public void deleteById(Long id) {
        if (!formRepository.existsById(id))
            throw new NonExistentIdException(Constants.NO_OBJECT_WITH_THIS_ID);
        formRepository.deleteById(id);
    }

    FormDto getFormDtoFromForm(Form form) {
        FormDto formDto = new FormDto();
        formDto.setId(form.getId());
        formDto.setName(form.getName());
        return formDto;
    }
}
