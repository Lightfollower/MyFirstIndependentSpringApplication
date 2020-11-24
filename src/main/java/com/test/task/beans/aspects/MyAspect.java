package com.test.task.beans.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

@Aspect
@Component
@Slf4j
public class MyAspect {
    @AfterReturning(pointcut = "execution(public * com.test.task.controllers.*Controller.get*ById(*))",
            returning = "result")
    public void getSomethingById(ResponseEntity result){
        log.info("Get entity: " + result.getBody());
    }

    @AfterReturning(pointcut = "execution(public * com.test.task.controllers.*Controller.saveNew*(*, *))",
            returning = "result")
    public void savingNewSomething(ResponseEntity result) {
        log.info("Saving new entity from " + result.getBody());
    }

    @AfterReturning(pointcut = "execution(public * com.test.task.controllers.*Controller.update*(*, *))",
            returning = "result")
    public void updatingSomething(ResponseEntity result) {
        log.info("Update entity from " + result.getBody());
    }

    @AfterReturning(pointcut = "execution(public * com.test.task.controllers.*Controller.delete*(*))")
    public void deletingSomething() {
        log.info("Deleting success");
    }
}
