package com.test.task.utils;

import com.test.task.entities.Bank;
import com.test.task.repositories.specifications.BankSpecifications;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

@Data
public class BankFilter {
    private Specification<Bank> spec;

    public BankFilter(Set<Long> banksIdSet) {
        this.spec = Specification.where(null);
        if (banksIdSet != null && !banksIdSet.isEmpty()) {
            Specification specBanks = null;
            for (Long b : banksIdSet) {
                if (specBanks == null) {
                    specBanks = BankSpecifications.clientIs(b);
                } else {
                    specBanks = specBanks.or(BankSpecifications.clientIs(b));
                }
            }
            spec = spec.and(specBanks);
        }
    }
}
