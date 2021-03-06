package com.test.task.services;

import com.test.task.entities.Deposit;
import com.test.task.entities.dtos.ClientDto;
import com.test.task.entities.dtos.DepositDto;
import com.test.task.exceptions.NonExistentIdException;
import com.test.task.repositories.DepositRepository;
import com.test.task.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepositService {
    private ClientService clientService;
    private BankService bankService;
    private DepositRepository depositRepository;

    @Autowired
    public DepositService(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @Autowired
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }

    public List<DepositDto> findAll(Specification<Deposit> spec, int pageNumber) {
        List<Deposit> deposits = depositRepository.findAll(spec, PageRequest.of(pageNumber, Constants.PAGE_SIZE)).getContent();
        return getDtoListFromDepositList(deposits);
    }

    public DepositDto saveOrUpdate(Deposit deposit) {
        return getDepositDtoFromDeposit(depositRepository.save(deposit), clientService.getDtoFromClient(deposit.getClient()));
    }

    public void deleteById(Long id) {
        if (!bankService.existsById(id))
            throw new NonExistentIdException(Constants.NO_OBJECT_WITH_THIS_ID);
        depositRepository.deleteById(id);
    }

    public List<DepositDto> getDtoListFromDepositList(List<Deposit> deposits) {
        List<DepositDto> depositDtos = new ArrayList<>();
        for (Deposit deposit :
                deposits) {
            depositDtos.add(getDepositDtoFromDeposit(deposit, clientService.getDtoFromClient(deposit.getClient())));
        }
        return depositDtos;
    }

    //    Метод на случай, если ClientDto будет содержать список вкладов
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
        depositDto.setBank(bankService.getBankDtoFromBank(deposit.getBank()));
        return depositDto;
    }
}
