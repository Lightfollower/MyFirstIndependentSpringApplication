package com.test.task.utils;

import com.test.task.entities.Bank;
import com.test.task.entities.Client;
import com.test.task.entities.Deposit;
import com.test.task.repositories.specifications.DepositSpecifications;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class DepositFilter {
    private Specification<Deposit> spec;

    public DepositFilter(Client clientFilter, Bank bankFilter) {
        this.spec = Specification.where(null);
        if (clientFilter != null) {
            spec = spec.and(DepositSpecifications.clientIs(clientFilter));
        }
        if (bankFilter != null) {
            spec = spec.and(DepositSpecifications.bankIs(bankFilter));
        }
    }
}
