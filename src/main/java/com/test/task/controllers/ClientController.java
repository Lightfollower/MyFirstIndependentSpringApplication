package com.test.task.controllers;

import com.test.task.entities.Bank;
import com.test.task.entities.Client;
import com.test.task.entities.Deposit;
import com.test.task.entities.Form;
import com.test.task.entities.dtos.ClientDto;
import com.test.task.services.BankService;
import com.test.task.services.ClientService;
import com.test.task.services.FormService;
import com.test.task.utils.ClientFilter;
import com.test.task.utils.ClientSorter;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @GetMapping("ololo")
//    public Set<Client> ololo(@RequestParam(name = "id") Long id) {
//        return getClientsByBank(id);
//    }

//    @GetMapping("asd")
//    public Client asd(){
//        return clientService.saveNewClient(new Client("Хуй Хуев", "буй", "фыв"));
//    }

    //    @GetMapping
//    public void ololo(@RequestParam Map<String, String> stringMap){
//        stringMap.entrySet().forEach(System.out::println);
//    }
    @GetMapping(produces = "application/json")
    public List<ClientDto> getClients(@RequestParam Map<String, String> requestParams/*, @RequestParam(name = "form", required = false) String form*/) {
        int pageNumber = Integer.parseInt(requestParams.getOrDefault(PAGE_STRING, "0"));
        Form formFilter = null;
        if (requestParams.containsKey(FORM_STRING)) {
            formFilter = formService.getByName(requestParams.get(FORM_STRING));
        }
        Set<Long> clientsByBankName = null;
        if (requestParams.containsKey(BANK_NAME)) {
            clientsByBankName = getClientsByBank(requestParams.get(BANK_NAME));
        }
        ClientFilter clientFilter = new ClientFilter(requestParams, formFilter, clientsByBankName);
        ClientSorter clientSorter = new ClientSorter(requestParams);
        return clientService.findAll(clientFilter.getSpec(), pageNumber, clientSorter.getSort());
    }

    @PostMapping(consumes = "application/json")
//    Deposits, Form, Id must be null.
    public Client add(@RequestBody Client client) {
        System.out.println(client);
        return clientService.saveNewClient(client);
    }

    private Set<Bank> getBanksOfClient(Long client) {
        Set<Bank> banks = new HashSet<>();
        List<Deposit> deposits = clientService.getClientById(client).getDeposits();
        for (Deposit d :
                deposits) {
            banks.add(d.getBank());
        }
        return banks;
    }

    private Set<Long> getClientsByBank(String bankName){
        Set<Long> clients = new HashSet<>();
        List<Deposit> deposits = bankService.getByName(bankName).getDeposits();
        for (Deposit d :
                deposits) {
            clients.add(d.getClient().getId());
        }
        return clients;
    }
}
