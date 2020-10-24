package com.test.task.repositories;

import com.test.task.entities.Deposit;
import com.test.task.entities.dtos.DepositDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {
    DepositDto getById(Long id);
}
