package com.test.task.controllers;

import com.test.task.entities.Bank;
import com.test.task.entities.Deposit;
import com.test.task.entities.dtos.BankDto;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.exceptions.NullIdException;
import com.test.task.services.BankService;
import com.test.task.services.ClientService;
import com.test.task.utils.BankFilter;
import com.test.task.utils.Constants;
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

import java.util.*;

@RestController
@RequestMapping("/api/v1/banks")
@Api("Set of endpoints for CRUD operations for banks")
@Slf4j
public class BankController {
    private BankService bankService;
    private ClientService clientService;

    @Autowired
    public BankController(BankService bankService, ClientService clientService) {
        this.bankService = bankService;
        this.clientService = clientService;
    }

    @GetMapping
    @ApiOperation("Returns list of all banks, selection by client name")
    public List<BankDto> getBanks(@RequestParam(required = false)
                                  @ApiParam("page - page number\nclient - client name") Map<String, String> requestParams) {
        log.info("Get banks");
        int pageNumber = Integer.parseInt(requestParams.getOrDefault(Constants.PAGE_STRING, "0"));
        Set<Long> banksByClientName = null;
        // Фильтрация банков по имени клиента
        if (requestParams.containsKey(Constants.CLIENT_STRING)) {
            log.info("Selecting banks by client name: " + requestParams.get(Constants.CLIENT_STRING));
            banksByClientName = getBanksByClient(requestParams.get(Constants.CLIENT_STRING));
        }
        if (banksByClientName.isEmpty())
            return Collections.EMPTY_LIST;
        BankFilter bankFilter = new BankFilter(banksByClientName);
        return bankService.findAll(bankFilter.getSpec(), pageNumber);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ApiOperation("Creates a new bank. If id != null, then it will be cleared")
    public ResponseEntity<?> saveNewBank(@RequestBody @Validated Bank bank, BindingResult bindingResult) {
        log.info("Saving new bank");
        if (bank.getId() != null)
            bank.setId(null);
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.allFieldsMustBeFilled);
        if (!bank.getBIC().matches("\\d*"))
            throw new MalformedEntityException("BIC can't contain non numeric symbols");
        return new ResponseEntity<>(bankService.saveOrUpdate(bank), HttpStatus.CREATED);
    }

    @PutMapping
    @ApiOperation("Updating an existing bank")
    public ResponseEntity<?> updateBank(@RequestBody @Validated Bank bank, BindingResult bindingResult) {
        log.info("Updating bank");
        if (bank.getId() == null)
            throw new NullIdException();
//        Оставлю проверки здесь, перенос их в сервисный слой не рационален.
        //        Если имена, старое и новое, не совпадают, тогда делается проверка на уникальность имени по базе.
        if (!bankService.getBankById(bank.getId()).getName().equals(bank.getName()))
//            Если не совпали имена, значит есть запрос на смену имени и его нужно проверить на уникальность
            if (bankService.existsByName(bank.getName()))
                throw new MalformedEntityException(String.format(Constants.nameIsBusy, bank.getName()));
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.allFieldsMustBeFilled);
//        В BankDto, из которого на фронт-енде собран этот Bank, отсутствует поле deposits. Нужно его заполнить.
        bank.setDeposits(bankService.getBankById(bank.getId()).getDeposits());
        return new ResponseEntity<>(bankService.saveOrUpdate(bank), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletes a bank from the system. 404 if the client's identifier is not found.")
    public void deleteBankById(@PathVariable Long id) {
        log.info("Deleting bank with id: " + id);
        bankService.deleteById(id);
    }

    //    Возвращает Set с Id банков клиента, имя которого передаётся в параметр clientName.
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
