package com.test.task.controllers;

import com.test.task.entities.Client;
import com.test.task.entities.Deposit;
import com.test.task.entities.Form;
import com.test.task.entities.dtos.ClientDto;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.exceptions.NullIdException;
import com.test.task.services.BankService;
import com.test.task.services.ClientService;
import com.test.task.services.FormService;
import com.test.task.utils.ClientFilter;
import com.test.task.utils.ClientSorter;
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
@RequestMapping("/api/v1/clients")
@Api("Set of endpoints for CRUD operations for clients")
@Slf4j
public class ClientController {
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
    @ApiOperation("Returns list of all clients, selection by name, bank, organization form, address and sort by name and form")
    public List<ClientDto> getClients(@RequestParam(required = false) @ApiParam(Constants.API_GET_CLIENTS) Map<String, String> requestParams) {
        log.info("Get clients");
        StringBuilder stringBuilder = new StringBuilder("Params:\n");
        for (Map.Entry<String, String> stringEntry :
                requestParams.entrySet()) {
            stringBuilder.append(stringEntry.toString() + " ");
        }
        log.info(stringBuilder.toString());
        int pageNumber = Integer.parseInt(requestParams.getOrDefault(Constants.PAGE_STRING, "0"));
        //        Фильтрация клиентов по организационной форме.
        Form formFilter = null;
        if (requestParams.containsKey(Constants.FORM_STRING)) {
            formFilter = formService.getByName(requestParams.get(Constants.FORM_STRING));
        }
//        Фильтрация клиентов по названию банка.
        Set<Long> clientsByBankName = null;
        if (requestParams.containsKey(Constants.BANK_STRING)) {
            log.info("Selecting clients by bank name: " + requestParams.get(Constants.BANK_STRING));
            clientsByBankName = getClientsByBank(requestParams.get(Constants.BANK_STRING));
            if (clientsByBankName.isEmpty())
                return Collections.EMPTY_LIST;
        }
        ClientFilter clientFilter = new ClientFilter(requestParams, formFilter, clientsByBankName);
        ClientSorter clientSorter = new ClientSorter(requestParams);
        return clientService.findAll(clientFilter.getSpec(), pageNumber, clientSorter.getSort());
    }

    @GetMapping("/{id}")
    @ApiOperation("Returns client by client id in path variable")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        log.info("Get client by id " + id);
        return new ResponseEntity<>(clientService.getClientDtoById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    @ApiOperation("Creates a new client. If id != null, then it will be cleared")
    public ResponseEntity<?> saveNewClient(@RequestBody @Validated Client client, BindingResult bindingResult) {
        log.info("Saving new client");
        if (client.getId() != null) {
            client.setId(null);
        }
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.ALL_FIELDS_MUST_BE_FILLED);
        if (client.getForm() == null)
            throw new MalformedEntityException("Choose an organizational form");
        return new ResponseEntity<>(clientService.saveOrUpdateClient(client), HttpStatus.CREATED);
    }

    @PutMapping(consumes = "application/json")
    @ApiOperation("Modifies an existing client")
    public ResponseEntity<?> updateClient(@RequestBody @Validated Client client, BindingResult bindingResult) {
        if (client.getId() == null)
            throw new NullIdException();
        log.info("Updating client with id " + client.getId());
//        Оставлю проверки здесь, перенос их в сервисный слой не рационален.
//        Если имена, старое и новое, не совпадают, тогда делается проверка на уникальность имени по базе.
        if (!clientService.getClientById(client.getId()).getName().equals(client.getName()))
//            Если не совпали имена, значит есть запрос на смену имени и его нужно проверить на уникальность.
            if (clientService.existsByName(client.getName()))
                throw new MalformedEntityException(String.format(Constants.NAME_IS_BUSY, client.getName()));
        if (bindingResult.hasErrors())
            throw new MalformedEntityException(Constants.ALL_FIELDS_MUST_BE_FILLED);
        if (client.getForm() == null)
            throw new MalformedEntityException("Choose an organizational form");
        //        В ClientDto, из которого на фронт-енде собран этот Client, отсутствует поле deposits. Нужно его заполнить.
        client.setDeposits(clientService.getClientById(client.getId()).getDeposits());
        return new ResponseEntity<>(clientService.saveOrUpdateClient(client), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Deletes a client from the system. 404 if the client's identifier is not found.")
    public void deleteClientById(@PathVariable Long id) {
        log.info("Deleting client with id " + id);
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
}
