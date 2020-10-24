package com.test.task.repositories;

import com.test.task.entities.Bank;
import com.test.task.entities.dtos.BankDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    BankDto getById(Long id);
}
