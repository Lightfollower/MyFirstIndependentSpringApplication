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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/deposits")
@Api("Set of endpoints for CRUD operations for deposits")
@Slf4j
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
    @ApiOperation("Returns list of all deposits, selection by client name and bank")
    public List<DepositDto> getDeposits(@RequestParam(required = false)
                                        @ApiParam("page - page number\nclient - client name\nbank - bank name") Map<String, String> requestParams) {
        log.info("Get deposits");
        int pageNumber = Integer.parseInt(requestParams.getOrDefault(Constants.PAGE_STRING, "0"));
        Client client = null;
//        Список вкладов по имени клиента
        if (requestParams.containsKey(Constants.CLIENT_STRING)) {
            client = clientService.getByName(requestParams.get(Constants.CLIENT_STRING));
            if(client.getDeposits().isEmpty())
                return Collections.emptyList();
        }
        Bank bank = null;
        //        Список вкладов по названию банка
        if (requestParams.containsKey(Constants.BANK_STRING)) {
            bank = bankService.getBankByName(requestParams.get(Constants.BANK_STRING));
            if(bank.getDeposits().isEmpty())
                return Collections.emptyList();}
        DepositFilter depositFilter = new DepositFilter(client, bank);
        return depositService.findAll(depositFilter.getSpec(), pageNumber);
    }


    //    Достаточно указать id клиента и банка в теле запроса
    @PostMapping
    @ApiOperation("Creates a new deposit. If id != null, then it will be cleared. It is enough to enter only id of client and bank")
    public ResponseEntity<?>saveNewDeposit(@RequestBody @Validated Deposit deposit, BindingResult bindingResult) {
        log.info("Saving new deposit");
        if (deposit.getId() != null) {
            deposit.setId(null);
        }
        if (bindingResult.hasErrors() || deposit.getClient() == null || deposit.getBank() == null ||
                deposit.getClient().getId() == null || deposit.getBank().getId() == null)
            throw new MalformedEntityException(Constants.ALL_FIELDS_MUST_BE_FILLED);
//        Нужно для того, чтобы указывать только id банка и клиента.
        deposit.setClient(clientService.getClientById(deposit.getClient().getId()));
        deposit.setBank(bankService.getBankById(deposit.getBank().getId()));

        return new ResponseEntity<>(depositService.saveOrUpdate(deposit), HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("Modifies an existing deposit. It is enough to enter only id of client and bank")
    public ResponseEntity<?> updateDeposit(@RequestBody @Validated Deposit deposit, BindingResult bindingResult) {
        log.info("Updating deposit");
        if (deposit.getId() == null) {
            throw new NullIdException();
        }
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.ALL_FIELDS_MUST_BE_FILLED);
//        Нужно для того, чтобы указывать только id банка и клиента.
        deposit.setClient(clientService.getClientById(deposit.getClient().getId()));
        deposit.setBank(bankService.getBankById(deposit.getBank().getId()));

        return new ResponseEntity<>(depositService.saveOrUpdate(deposit), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletes a deposit from the system. 404 if the deposit's identifier is not found.")
    public void deleteDepositById(@PathVariable Long id) {
       log.info("Deleting deposit with id: " + id);
        depositService.deleteById(id);
    }
}
