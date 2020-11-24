package com.test.task.services;

import com.test.task.entities.Client;
import com.test.task.entities.dtos.ClientDto;
import com.test.task.exceptions.NonExistentIdException;
import com.test.task.repositories.ClientRepository;
import com.test.task.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientService {
    private ClientRepository clientRepository;
    private FormService formService;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @Autowired
    public void setFormService(FormService formService) {
        this.formService = formService;
    }

    public List<ClientDto> findAll(Specification<Client> spec, int page, Sort sort) {
        if (page < 1L) {
            page = 0;
        }
        List<ClientDto> clientDtoList = new ArrayList<>();
        List<Client> clientList = clientRepository.findAll(spec, PageRequest.of(page, Constants.PAGE_SIZE, sort)).getContent();
        for (Client client :
                clientList) {
            clientDtoList.add(getDtoFromClient(client));
        }
        return clientDtoList;
    }

    public ClientDto saveOrUpdateClient(Client client) {
        return getDtoFromClient(clientRepository.save(client));
    }

    public Client getClientById(Long id) {
        if (!clientRepository.existsById(id))
            throw new NonExistentIdException("No client with same id");
        return clientRepository.getById(id);
    }

    public Client getByName(String name) {
        if (!clientRepository.existsByName(name))
            throw new ResourceNotFoundException("No client with same name");
        return clientRepository.getByName(name);
    }

    public ClientDto getClientDtoById(Long id) {
        return getDtoFromClient(clientRepository.getById(id));
    }

    public boolean existsById(Long id) {
        return clientRepository.existsById(id);
    }

    public boolean existsByName(String name) {
        return clientRepository.existsByName(name);
    }

    public void deleteById(Long id) {
        clientRepository.deleteById(id);
    }

    ClientDto getDtoFromClient(Client client) {
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setName(client.getName());
        clientDto.setShortName(client.getShortName());
        clientDto.setAddress(client.getAddress());
        clientDto.setForm(formService.getFormDtoFromForm(client.getForm()));
        return clientDto;
    }
}
