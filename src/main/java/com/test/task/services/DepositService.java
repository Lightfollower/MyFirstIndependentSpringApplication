package com.test.task.services;

import com.test.task.entities.Deposit;
import com.test.task.entities.dtos.DepositDto;
import com.test.task.repositories.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepositService {
    ClientService clientService;
    DepositRepository depositRepository;

    @Autowired
    public DepositService(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public DepositDto getDepositById(Long id){
        return depositRepository.getById(id);
    }

    public List<DepositDto> getDtoFromDeposit(List<Deposit> deposits) {
        List<DepositDto> depositDtos = new ArrayList<>();
        for (Deposit deposit :
                deposits) {
            DepositDto depositDto = new DepositDto();
            depositDto.setId(deposit.getId());
            depositDto.setDate(deposit.getDate());
            depositDto.setRate(deposit.getRate());
            depositDto.setTerm(deposit.getTerm());
            depositDto.setClient(deposit.getClient().getName());
            depositDtos.add(depositDto);
        }
        return depositDtos;
    }
}
