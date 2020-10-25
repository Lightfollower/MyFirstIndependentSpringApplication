package com.test.task.services;

import com.test.task.entities.Client;
import com.test.task.entities.dtos.ClientDto;
import com.test.task.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    final int PAGE_SIZE = 5;
    ClientRepository clientRepository;
    DepositService depositService;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }
    @Autowired
    public void setDepositService(DepositService depositService) {
        this.depositService = depositService;
    }

    public List<ClientDto> findAll(Specification<Client> spec, int page) {
        if (page < 1L) {
            page = 0;
        }
        List<ClientDto> clientDtoList = new ArrayList<>();
        List<Client> clientList = clientRepository.findAll(spec, PageRequest.of(page, PAGE_SIZE)).getContent();
        for (Client client :
                clientList) {
            clientDtoList.add(getDtoFromClient(client));
        }
        return clientDtoList;
    }

    public Page<ClientDto> findAllBy(Specification<Client> spec, int page) {
        if (page < 1L) {
            page = 1;
        }
        return clientRepository.findAllBy(spec, PageRequest.of(page, PAGE_SIZE));
    }

    public Client saveNewClient(Client client) {
        return clientRepository.save(client);
    }

    public Client getClientById(Long i) {
        return clientRepository.getById(i);
    }

    public ClientDto getDtoFromClient(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setShortName(client.getShortName());
        clientDto.setAddress(client.getAddress());
        clientDto.setForm(client.getForm());
        clientDto.setDeposits(depositService.getDtoListFromDepositList(client.getDeposits(), clientDto));
        return clientDto;
    }
}
