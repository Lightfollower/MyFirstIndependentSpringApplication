package com.test.task.beans.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MyAspect {
    @AfterReturning(pointcut = "execution(public * com.test.task.controllers.*Controller.get*(*))")
    public void getSomething() {
        log.info("Get success");
    }

    @AfterReturning(pointcut = "execution(public * com.test.task.controllers.*Controller.saveNew*(..))",
            returning = "result")
    public void savingNewSomething(ResponseEntity result) {
        log.info("Save success: " + result.getBody());
    }

    @AfterReturning(pointcut = "execution(public * com.test.task.controllers.*Controller.update*(..))",
            returning = "result")
    public void updatingSomething(ResponseEntity result) {
        log.info("Update success: " + result.getBody());
    }

    @AfterReturning(pointcut = "execution(public * com.test.task.controllers.*Controller.delete*(*))")
    public void deletingSomething() {
        log.info("Deleting success");
    }
}
