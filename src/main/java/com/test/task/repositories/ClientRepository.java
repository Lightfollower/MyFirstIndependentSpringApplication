package com.test.task.repositories;

import com.test.task.entities.Client;
import com.test.task.entities.dtos.ClientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    Client getById(Long id);
    Client getByName(String name);
    Page<ClientDto> findAllBy(Specification<Client> spec, Pageable pageable);
}
