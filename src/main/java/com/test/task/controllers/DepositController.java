package com.test.task.controllers;

import com.test.task.entities.Deposit;
import com.test.task.entities.dtos.DepositDto;
import com.test.task.services.BankService;
import com.test.task.services.ClientService;
import com.test.task.services.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deposit")
public class DepositController {
    private DepositService depositService;
    private ClientService clientService;
    private BankService bankService;

    @Autowired
    public DepositController(DepositService depositService, ClientService clientService, BankService bankService) {
        this.depositService = depositService;
        this.clientService = clientService;
        this.bankService = bankService;
    }

    @GetMapping
    public List<DepositDto> findAll(){
        return depositService.findAll();
    }

    @PostMapping
    public DepositDto add(@RequestBody Deposit deposit){
        if (deposit.getId() != null) {
            deposit.setId(null);
        }
        deposit.setClient(clientService.getClientById(deposit.getClient().getId()));
        deposit.setBank(bankService.getBankById(deposit.getBank().getId()));
        return depositService.saveOrUpdate(deposit);
    }

    @PutMapping
    public DepositDto modify(@RequestBody Deposit deposit){
        if (deposit.getId() == null) {
            throw new RuntimeException("Deposit id can't be null");
        }
        return depositService.saveOrUpdate(deposit);
    }
}
