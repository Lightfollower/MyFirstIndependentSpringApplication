package com.test.task.services;

import com.test.task.entities.Deposit;
import com.test.task.entities.dtos.ClientDto;
import com.test.task.entities.dtos.DepositDto;
import com.test.task.repositories.DepositRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepositService {
    private ClientService clientService;
    private DepositRepository depositRepository;

    @Autowired
    public DepositService(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public List<Deposit> findAll(){
        return depositRepository.findAll();
    }

    public Deposit save(Deposit deposit){
        return depositRepository.save(deposit);
    }

    public DepositDto getDepositById(Long id){
        return depositRepository.getById(id);
    }

    public List<DepositDto> getDtoListFromDepositList(List<Deposit> deposits) {
        List<DepositDto> depositDtos = new ArrayList<>();
        for (Deposit deposit :
                deposits) {
            depositDtos.add(getDepositDtoFromDeposit(deposit, clientService.getDtoFromClient(deposit.getClient())));
        }
        return depositDtos;
    }

    public List<DepositDto> getDtoListFromDepositList(List<Deposit> deposits, ClientDto clientDto) {
        List<DepositDto> depositDtos = new ArrayList<>();
        for (Deposit deposit :
                deposits) {
            depositDtos.add(getDepositDtoFromDeposit(deposit, clientDto));
        }
        return depositDtos;
    }

    private DepositDto getDepositDtoFromDeposit(Deposit deposit, ClientDto clientDto) {
        DepositDto depositDto = new DepositDto();
        depositDto.setId(deposit.getId());
        depositDto.setDate(deposit.getDate());
        depositDto.setRate(deposit.getRate());
        depositDto.setTerm(deposit.getTerm());
        depositDto.setClient(clientDto);
        return depositDto;
    }
}
