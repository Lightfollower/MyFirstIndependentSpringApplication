package com.test.task.controllers;

import com.test.task.entities.Client;
import com.test.task.entities.Form;
import com.test.task.entities.dtos.ClientDto;
import com.test.task.services.ClientService;
import com.test.task.services.FormService;
import com.test.task.utils.ClientFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
//@CrossOrigin("*")
@RequestMapping("/api/v1/clients")
//@Api("Set of endpoints for CRUD operations for Products")
public class ClientController {
    private ClientService clientService;
    private FormService formService;

    @Autowired
    public ClientController(ClientService clientService, FormService formService) {
        this.clientService = clientService;
        this.formService = formService;
    }
//    @GetMapping("ololo")
//    public String ololo(){
//        return clientService.getClientById(1L).getDeposits().get(0).getId().toString();
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
    public List<ClientDto> getClients(@RequestParam Map<String, String> requestParams, @RequestParam(name = "form", required = false) String form){
        int pageNumber = Integer.parseInt(requestParams.getOrDefault("p", "0"));
        Form formFilter = formService.getByName(form);
        ClientFilter clientFilter = new ClientFilter(requestParams, formFilter);
        return clientService.findAll(clientFilter.getSpec(), pageNumber);
    }

    @PostMapping()
    public void add(@RequestBody Client client){
        System.out.println(client);
        System.out.println(client);
        System.out.println(client);
        System.out.println(client);
        clientService.saveNewClient(client);
//        return clientService.getClientById(client.getId());
    }
}
