package com.test.task.services;

import com.test.task.entities.Bank;
import com.test.task.entities.dtos.BankDto;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.exceptions.NonExistentIdException;
import com.test.task.repositories.BankRepository;
import com.test.task.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankService {
    private BankRepository bankRepository;

    @Autowired
    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public Bank getBankById(Long id) {
        if (!bankRepository.existsById(id))
            throw new NonExistentIdException(String.format(Constants.NO_OBJECT_WITH_THIS_ID, Constants.BANK_STRING));
        return bankRepository.getById(id);
    }

    public Bank getBankByName(String name) {
        if (!bankRepository.existsByName(name))
            throw new ResourceNotFoundException(String.format(Constants.NO_OBJECT_WITH_THIS_NAME, Constants.BANK_STRING));
        return bankRepository.getByName(name);
    }

    public List<BankDto> findAll(Specification<Bank> spec, int pageNumber) {
        List<Bank> banks = bankRepository.findAll(spec, PageRequest.of(pageNumber, Constants.PAGE_SIZE)).getContent();
        return getDtoListFromBankList(banks);
    }

    public BankDto saveOrUpdate(Bank bank) {
        if (bankRepository.existsByName(bank.getName()))
            throw new MalformedEntityException(String.format(Constants.NAME_IS_BUSY, bank.getName()));
        return getBankDtoFromBank(bankRepository.save(bank));
    }

    public void deleteById(Long id) {
        if (!bankRepository.existsById(id))
            throw new NonExistentIdException(String.format(Constants.NO_OBJECT_WITH_THIS_ID, Constants.BANK_STRING));
        bankRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return bankRepository.existsById(id);
    }

    public boolean existsByName(String name) {
        return bankRepository.existsByName(name);
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
