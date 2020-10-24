package com.test.task.repositories;

import com.test.task.entities.Form;
import com.test.task.entities.dtos.FormDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    FormDto getById(Long id);
}
