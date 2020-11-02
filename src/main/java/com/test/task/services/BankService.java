package com.test.task.services;

import com.test.task.entities.Bank;
import com.test.task.entities.dtos.BankDto;
import com.test.task.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankService {
    BankRepository bankRepository;
    DepositService depositService;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Autowired
    public void setDepositService(DepositService depositService) {
        this.depositService = depositService;
    }

    public Bank getBankById(Long id){
        return bankRepository.getById(id);
    }

    public Bank getByName(String name){
        return bankRepository.getByName(name);
    }

    public BankDto getDtoFromBank(Bank bank) {
        BankDto bankDto = new BankDto();
        bankDto.setId(bank.getId());
        bankDto.setName(bank.getName());
        bankDto.setBIC(bank.getBIC());
//        bankDto.setDeposits(depositService.getDtoListFromDepositList(bank.getDeposits()));
        return bankDto;
    }
}
