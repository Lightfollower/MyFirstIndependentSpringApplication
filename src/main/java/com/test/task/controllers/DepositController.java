package com.test.task.controllers;

import com.test.task.entities.Bank;
import com.test.task.entities.Client;
import com.test.task.entities.Deposit;
import com.test.task.entities.dtos.DepositDto;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.exceptions.NullIdException;
import com.test.task.services.BankService;
import com.test.task.services.ClientService;
import com.test.task.services.DepositService;
import com.test.task.utils.Constants;
import com.test.task.utils.DepositFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/deposits")
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
    public List<DepositDto> getDeposits(@RequestParam Map<String, String> requestParams) {
        int pageNumber = Integer.parseInt(requestParams.getOrDefault(Constants.PAGE_STRING, "0"));
        Client client = null;
//        Список вкладов по имени клиента
        if (requestParams.containsKey(Constants.CLIENT_STRING)) {
            client = clientService.getByName(requestParams.get(Constants.CLIENT_STRING));
        }
        Bank bank = null;
        //        Список вкладов по названию банка
        if (requestParams.containsKey(Constants.BANK_NAME)) {
            bank = bankService.getBankByName(requestParams.get(Constants.BANK_NAME));
        }
        DepositFilter depositFilter = new DepositFilter(client, bank);
        return depositService.findAll(depositFilter.getSpec(), pageNumber);
    }


    //    Достаточно указать id клиента и банка в теле запроса
    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Validated Deposit deposit, BindingResult bindingResult) {
        if (deposit.getId() != null) {
            deposit.setId(null);
        }
        if (bindingResult.hasErrors() || deposit.getClient() == null || deposit.getBank() == null ||
        deposit.getClient().getId() == null || deposit.getBank().getId() == null)
            throw new MalformedEntityException(Constants.allFieldsMustBeFilled);
//        Нужно для того, чтобы указывать только id банка и клиента.
        deposit.setClient(clientService.getClientById(deposit.getClient().getId()));
        deposit.setBank(bankService.getBankById(deposit.getBank().getId()));

        return new ResponseEntity<>(depositService.saveOrUpdate(deposit), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> modify(@RequestBody @Validated Deposit deposit, BindingResult bindingResult) {
        if (deposit.getId() == null) {
            throw new NullIdException();
        }
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.allFieldsMustBeFilled);
//        Нужно для того, чтобы указывать только id банка и клиента.
        deposit.setClient(clientService.getClientById(deposit.getClient().getId()));
        deposit.setBank(bankService.getBankById(deposit.getBank().getId()));

        return new ResponseEntity<>(depositService.saveOrUpdate(deposit), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        depositService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
