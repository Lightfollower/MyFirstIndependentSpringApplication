package com.test.task.repositories.specifications;

import com.test.task.entities.Bank;
import com.test.task.entities.Client;
import com.test.task.entities.Deposit;
import org.springframework.data.jpa.domain.Specification;

public class DepositSpecifications {
    public static Specification<Deposit> clientIs(Client client) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("client"), client);
    }

    public static Specification<Deposit> bankIs(Bank bank) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("bank"), bank);
    }
}
