package com.test.task.services;

import com.test.task.entities.Bank;
import com.test.task.entities.dtos.BankDto;
import com.test.task.repositories.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankService {
    private static final int PAGE_SIZE = 5;
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

    public Bank getBankByName(String name){
        return bankRepository.getByName(name);
    }



    public List<BankDto> findAll(Specification<Bank> spec, int pageNumber) {
        List<Bank> banks = bankRepository.findAll(spec, PageRequest.of(pageNumber, PAGE_SIZE)).getContent();
        return getDtoListFromBankList(banks);
    }

    public BankDto saveOrUpdate(Bank bank){
        return getBankDtoFromBank(bankRepository.save(bank));
    }

    public boolean existsById(Long id) {
        return bankRepository.existsById(id);
    }

    public boolean existsByName(String name) {
        return bankRepository.existsByName(name);
    }

    public void deleteById(Long id) {
        bankRepository.deleteById(id);
    }

    private List<BankDto> getDtoListFromBankList(List<Bank> banks) {
        List<BankDto> bankDtos = new ArrayList<>();
        for (Bank bank :
                banks) {
            bankDtos.add(getBankDtoFromBank(bank));
        }
        return bankDtos;
    }

    BankDto getBankDtoFromBank(Bank bank) {
        BankDto bankDto = new BankDto();
        bankDto.setId(bank.getId());
        bankDto.setName(bank.getName());
        bankDto.setBIC(bank.getBIC());
        return bankDto;
    }



}
