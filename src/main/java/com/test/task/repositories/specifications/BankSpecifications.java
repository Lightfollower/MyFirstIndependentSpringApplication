package com.test.task.repositories.specifications;

import com.test.task.entities.Bank;
import org.springframework.data.jpa.domain.Specification;

public class BankSpecifications {
    public static Specification<Bank> clientIs(Long client) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("id"), client);
    }
}
