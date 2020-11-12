package com.test.task.controllers;

import com.test.task.entities.Bank;
import com.test.task.entities.Client;
import com.test.task.entities.Deposit;
import com.test.task.entities.dtos.DepositDto;
import com.test.task.services.BankService;
import com.test.task.services.ClientService;
import com.test.task.services.DepositService;
import com.test.task.utils.DepositFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/deposits")
public class DepositController {
    private static final String CLIENT_STRING = "client";
    private static final String BANK_NAME = "bank";
    private static final String PAGE_STRING = "page";
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
    public List<DepositDto> getDeposits(@RequestParam Map<String, String> requestParams){
        int pageNumber = Integer.parseInt(requestParams.getOrDefault(PAGE_STRING, "0"));
        Client client = null;
//        Список вкладов по имени клиента
        if (requestParams.containsKey(CLIENT_STRING)) {
            client = clientService.getByName(requestParams.get(CLIENT_STRING));
        }
        Bank bank = null;
        //        Список вкладов по названию банка
        if (requestParams.containsKey(BANK_NAME)) {
            bank = bankService.getByName(requestParams.get(BANK_NAME));
        }
        DepositFilter depositFilter = new DepositFilter(client, bank);
        return depositService.findAll(depositFilter.getSpec(), pageNumber);
    }


//    Достаточно указать id клиента и банка в теле запроса
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

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        depositService.deleteById(id);
    }
}
