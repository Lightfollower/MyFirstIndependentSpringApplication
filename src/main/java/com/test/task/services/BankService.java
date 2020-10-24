package com.test.task.services;

import com.test.task.entities.Bank;
import com.test.task.entities.dtos.BankDto;
import com.test.task.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {
    BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public BankDto getBank(){
        return bankRepository.getById(1L);
    }
}
