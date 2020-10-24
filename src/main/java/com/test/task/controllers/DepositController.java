package com.test.task.controllers;

import com.test.task.entities.Deposit;
import com.test.task.entities.dtos.DepositDto;
import com.test.task.services.DepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deposit")
public class DepositController {
    DepositService depositService;

    @Autowired
    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    @GetMapping("ol")
    public DepositDto ol(){
        return depositService.getDepositById(1L);
    }

    @PostMapping
    public void add(@RequestBody Deposit deposit){
        System.out.println(deposit);
        System.out.println(deposit);
        System.out.println(deposit);
        System.out.println(deposit);
    }
}
