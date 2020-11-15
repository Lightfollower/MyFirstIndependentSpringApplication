package com.test.task.controllers;

import com.test.task.entities.Client;
import com.test.task.entities.Deposit;
import com.test.task.entities.Form;
import com.test.task.entities.dtos.ClientDto;
import com.test.task.exceptions.MalformedEntityException;
import com.test.task.exceptions.NullIdException;
import com.test.task.exceptions.nonExistentIdException;
import com.test.task.services.BankService;
import com.test.task.services.ClientService;
import com.test.task.services.FormService;
import com.test.task.utils.ClientFilter;
import com.test.task.utils.ClientSorter;
import com.test.task.utils.Constants;
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
        int pageNumber = Integer.parseInt(requestParams.getOrDefault(Constants.PAGE_STRING, "0"));
        //        Фильтрация клиентов по организационной форме.
        Form formFilter = null;
        if (requestParams.containsKey(Constants.FORM_STRING)) {
            formFilter = formService.getByName(requestParams.get(Constants.FORM_STRING));
        }
//        Фильтрация клиентов по названию банка.
        Set<Long> clientsByBankName = null;
        if (requestParams.containsKey(Constants.BANK_NAME)) {
            clientsByBankName = getClientsByBank(requestParams.get(Constants.BANK_NAME));
        }
        ClientFilter clientFilter = new ClientFilter(requestParams, formFilter, clientsByBankName);
        ClientSorter clientSorter = new ClientSorter(requestParams);
        return clientService.findAll(clientFilter.getSpec(), pageNumber, clientSorter.getSort());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        if (!clientService.existsById(id))
            throw new nonExistentIdException(Constants.noObjectWithThisId);
        return new ResponseEntity<>(clientService.getClientDtoById(id), HttpStatus.OK);
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> add(@RequestBody @Validated Client client, BindingResult bindingResult) {
        if (client.getId() != null) {
            client.setId(null);
        }
        if (clientService.existsByName(client.getName()))
            throw new MalformedEntityException(String.format(Constants.nameIsBusy, client.getName()));
        if (bindingResult.hasErrors())
            throw new MalformedEntityException();
        if (client.getForm() == null)
            throw new MalformedEntityException("Choose an organizational form");
        return new ResponseEntity<>(clientService.saveOrUpdateClient(client), HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json")
    public ResponseEntity<?> modifyClient(@RequestBody @Validated Client client, BindingResult bindingResult) {
        if (client.getId() == null)
            throw new NullIdException();
        if (!clientService.existsById(client.getId()))
            throw new nonExistentIdException(Constants.noObjectWithThisId);
//        Если имена, старое и новое, не совпадают, тогда делается проверка на уникальность имени по базе.
        if (!clientService.getClientById(client.getId()).getName().equals(client.getName()))
//            Если не совпали имена, значит есть запрос на смену имени и его нужно проверить на уникальность
            if (clientService.existsByName(client.getName()))
                throw new MalformedEntityException(String.format(Constants.nameIsBusy, client.getName()));
        if (bindingResult.hasErrors())
            throw new MalformedEntityException();
        if (client.getForm() == null)
            throw new MalformedEntityException("Choose an organizational form");
        client.setDeposits(clientService.getClientById(client.getId()).getDeposits());
        return new ResponseEntity<>(clientService.saveOrUpdateClient(client), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        if (!clientService.existsById(id))
            throw new nonExistentIdException(Constants.noObjectWithThisId);
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
