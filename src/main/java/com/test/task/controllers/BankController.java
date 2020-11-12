package com.test.task.controllers;

import com.test.task.entities.Bank;
import com.test.task.entities.Client;
import com.test.task.entities.Deposit;
import com.test.task.entities.dtos.BankDto;
import com.test.task.services.BankService;
import com.test.task.services.ClientService;
import com.test.task.utils.BankFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/banks")
public class BankController {
    private static final String PAGE_STRING = "page";
    private static final String CLIENT_NAME = "client";
    BankService bankService;
    ClientService clientService;

    @Autowired
    public BankController(BankService bankService, ClientService clientService) {
        this.bankService = bankService;
        this.clientService = clientService;
    }

    @GetMapping
    public List<BankDto> getBanks(@RequestParam Map<String, String> requestParams) {
        int pageNumber = Integer.parseInt(requestParams.getOrDefault(PAGE_STRING, "0"));
        Set<Long> banksByClientName = null;
        // Фильтрация банков по имени клиента
        if (requestParams.containsKey(CLIENT_NAME)) {
            banksByClientName = getBanksByClient(requestParams.get(CLIENT_NAME));
        }
        BankFilter bankFilter = new BankFilter(banksByClientName);

        return bankService.findAll(bankFilter.getSpec(), pageNumber);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public BankDto add(@RequestBody Bank bank) {
        if (bank.getId() != null)
            bank.setId(null);
        return bankService.saveOrUpdate(bank);
    }

    //    Возвращает Set с Id банков клиента, название которого передаётся в параметр clientName.
    private Set<Long> getBanksByClient(String clientName) {
        Set<Long> banks = new HashSet<>();
        List<Deposit> deposits = clientService.getByName(clientName).getDeposits();
        for (Deposit d :
                deposits) {
            banks.add(d.getBank().getId());
        }
        return banks;
    }


}
