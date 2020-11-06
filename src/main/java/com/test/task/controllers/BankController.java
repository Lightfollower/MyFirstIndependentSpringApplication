package com.test.task.controllers;

import com.test.task.entities.Bank;
import com.test.task.entities.dtos.BankDto;
import com.test.task.services.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/banks")
public class BankController {
    BankService bankService;

    @Autowired
    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    public List<BankDto> getBanks(){
       return bankService.findAll();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public void add(@RequestBody Bank bank){
    }
}
