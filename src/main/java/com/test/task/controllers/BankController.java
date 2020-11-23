package com.test.task.controllers;

import com.test.task.entities.Bank;
import com.test.task.entities.Deposit;
import com.test.task.entities.dtos.BankDto;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.exceptions.NullIdException;
import com.test.task.exceptions.nonExistentIdException;
import com.test.task.services.BankService;
import com.test.task.services.ClientService;
import com.test.task.utils.BankFilter;
import com.test.task.utils.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/banks")
@Api("Set of endpoints for CRUD operations for banks")
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
        int pageNumber = Integer.parseInt(requestParams.getOrDefault(Constants.PAGE_STRING, "0"));
        Set<Long> banksByClientName = null;
        // Фильтрация банков по имени клиента
        if (requestParams.containsKey(Constants.CLIENT_STRING)) {
            banksByClientName = getBanksByClient(requestParams.get(Constants.CLIENT_STRING));
        }
        BankFilter bankFilter = new BankFilter(banksByClientName);

        return bankService.findAll(bankFilter.getSpec(), pageNumber);
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ApiOperation("Creates a new bank. If id != null, then it will be cleared")
    public ResponseEntity<?> add(@RequestBody @Validated Bank bank, BindingResult bindingResult) {
        if (bank.getId() != null)
            bank.setId(null);
        if (bankService.existsByName(bank.getName()))
            throw new MalformedEntityException(String.format(Constants.nameIsBusy, bank.getName()));
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.allFieldsMustBeFilled);
        if (!bank.getBIC().matches("\\d*"))
            throw new MalformedEntityException("BIC can't contain non numeric symbols");
        return new ResponseEntity<>(bankService.saveOrUpdate(bank), HttpStatus.OK);
    }

    @PutMapping
    @ApiOperation("Modifies an existing bank")
    public ResponseEntity<?> update(@RequestBody @Validated Bank bank, BindingResult bindingResult) {
        if (bank.getId() == null)
            throw new NullIdException();
        //        Если имена, старое и новое, не совпадают, тогда делается проверка на уникальность имени по базе.
        if (!bankService.getBankById(bank.getId()).getName().equals(bank.getName()))
//            Если не совпали имена, значит есть запрос на смену имени и его нужно проверить на уникальность
            if (bankService.existsByName(bank.getName()))
                throw new MalformedEntityException(String.format(Constants.nameIsBusy, bank.getName()));
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.allFieldsMustBeFilled);
        bank.setDeposits(bankService.getBankById(bank.getId()).getDeposits());
        return new ResponseEntity<>(bankService.saveOrUpdate(bank), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletes a client from the system. 404 if the client's identifier is not found.")
    public void delete(@PathVariable Long id) {
        if (!bankService.existsById(id))
            throw new nonExistentIdException(Constants.noObjectWithThisId);
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
