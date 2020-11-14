package com.test.task.controllers;

import com.test.task.entities.Client;
import com.test.task.entities.Deposit;
import com.test.task.entities.Form;
import com.test.task.entities.dtos.ClientDto;
import com.test.task.exceptions.BadEntityException;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.exceptions.NonExistentIdException;
import com.test.task.services.BankService;
import com.test.task.services.ClientService;
import com.test.task.services.FormService;
import com.test.task.utils.ClientFilter;
import com.test.task.utils.ClientSorter;
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
//@CrossOrigin("*")
@RequestMapping("/api/v1/clients")
//@Api("Set of endpoints for CRUD operations for Products")
public class ClientController {
    private static final String PAGE_STRING = "page";
    private static final String FORM_STRING = "form";
    private static final String BANK_NAME = "bank";
    private ClientService clientService;
    private FormService formService;
    private BankService bankService;

    @Autowired
    public ClientController(ClientService clientService, FormService formService, BankService bankService) {
        this.clientService = clientService;
        this.formService = formService;
        this.bankService = bankService;
    }

    @GetMapping(produces = "application/json")
    public List<ClientDto> getClients(@RequestParam Map<String, String> requestParams/*, @RequestParam(name = "form", required = false) String form*/) {
        int pageNumber = Integer.parseInt(requestParams.getOrDefault(PAGE_STRING, "0"));
        //        Фильтрация клиентов по организационной форме.
        Form formFilter = null;
        if (requestParams.containsKey(FORM_STRING)) {
            formFilter = formService.getByName(requestParams.get(FORM_STRING));
        }
//        Фильтрация клиентов по названию банка.
        Set<Long> clientsByBankName = null;
        if (requestParams.containsKey(BANK_NAME)) {
            clientsByBankName = getClientsByBank(requestParams.get(BANK_NAME));
        }
        ClientFilter clientFilter = new ClientFilter(requestParams, formFilter, clientsByBankName);
        ClientSorter clientSorter = new ClientSorter(requestParams);
        return clientService.findAll(clientFilter.getSpec(), pageNumber, clientSorter.getSort());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        if (!clientService.existsById(id))
            throw new NonExistentIdException("No object with this id");
        return new ResponseEntity<>(clientService.getClientDtoById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> add(@RequestBody @Validated Client client, BindingResult bindingResult) {
        if (client.getId() != null) {
            client.setId(null);
        }
        if (bindingResult.hasErrors())
            throw new BadEntityException("All fields must be filled");
        if (client.getForm() == null) {
            throw new BadEntityException("Choose an organizational form");
        }
        return new ResponseEntity<>(clientService.saveOrUpdateClient(client), HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json")
    public ClientDto modifyClient(@RequestBody Client client) {
        if (client.getId() == null) {
            throw new RuntimeException("client id can't be null");
        }
        return clientService.saveOrUpdateClient(client);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        clientService.deleteById(id);
    }

    //    Возвращает Set с Id клиентов банка, название которого передаётся в параметр bankName.
    private Set<Long> getClientsByBank(String bankName) {
        Set<Long> clients = new HashSet<>();
        List<Deposit> deposits = bankService.getBankByName(bankName).getDeposits();
        for (Deposit d :
                deposits) {
            clients.add(d.getClient().getId());
        }
        return clients;
    }

  @ExceptionHandler
    public ResponseEntity<?> handleIdException(NonExistentIdException exc) {
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleSomeException(RuntimeException exc) {
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleBadEntityException(MalformedEntityException exc) {
        return new ResponseEntity<>(exc.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
