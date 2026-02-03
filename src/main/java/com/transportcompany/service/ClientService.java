package com.transportcompany.service;

import com.transportcompany.entity.Client;
import com.transportcompany.entity.TransportCompany;
import com.transportcompany.repository.ClientRepository;
import com.transportcompany.repository.TransportCompanyRepository;
import com.transportcompany.validation.ValidationException;
import com.transportcompany.validation.ValidationUtil;
import java.util.List;
import java.util.Optional;

public class ClientService {

    private final ClientRepository repository;
    private final TransportCompanyRepository companyRepository;

    public ClientService() {
        this.repository = new ClientRepository();
        this.companyRepository = new TransportCompanyRepository();
    }

    public Client create(Long companyId, String name, String contactPerson, String phone, String email) {
        TransportCompany company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company not found with id: " + companyId));

        Client client = new Client(name, contactPerson, phone, email);
        client.setCompany(company);

        ValidationUtil.validate(client);
        return repository.save(client);
    }

    public Client update(Client client) {
        ValidationUtil.validate(client);
        return repository.update(client);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<Client> findById(Long id) {
        return repository.findById(id);
    }

    public Client findByIdWithTransports(Long id) {
        return repository.findByIdWithTransports(id);
    }

    public List<Client> findByCompanyId(Long companyId) {
        return repository.findByCompanyId(companyId);
    }

    public Optional<Client> findByNameAndCompany(String name, Long companyId) {
        return repository.findByNameAndCompany(name, companyId);
    }

    public List<Client> findClientsWithUnpaidTransports(Long companyId) {
        return repository.findClientsWithUnpaidTransports(companyId);
    }

    public long count() {
        return repository.count();
    }
}
