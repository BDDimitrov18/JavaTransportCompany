package com.transportcompany.service;

import com.transportcompany.entity.TransportCompany;
import com.transportcompany.repository.TransportCompanyRepository;
import com.transportcompany.validation.ValidationUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class TransportCompanyService {

    private final TransportCompanyRepository repository;

    public TransportCompanyService() {
        this.repository = new TransportCompanyRepository();
    }

    public TransportCompany create(String name, String address) {
        TransportCompany company = new TransportCompany(name, address);
        ValidationUtil.validate(company);
        return repository.save(company);
    }

    public TransportCompany update(TransportCompany company) {
        ValidationUtil.validate(company);
        return repository.update(company);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<TransportCompany> findById(Long id) {
        return repository.findById(id);
    }

    public TransportCompany findByIdWithDetails(Long id) {
        return repository.findByIdWithDetails(id);
    }

    public Optional<TransportCompany> findByName(String name) {
        return repository.findByName(name);
    }

    public List<TransportCompany> findAll() {
        return repository.findAll();
    }

    public List<TransportCompany> findAllSortedByName() {
        return repository.findAllSortedByName();
    }

    public List<TransportCompany> findAllSortedByRevenue() {
        return repository.findAllSortedByRevenue();
    }

    public void updateRevenue(Long companyId, BigDecimal amount) {
        Optional<TransportCompany> companyOpt = repository.findById(companyId);
        if (companyOpt.isPresent()) {
            TransportCompany company = companyOpt.get();
            company.setRevenue(company.getRevenue().add(amount));
            repository.update(company);
        }
    }

    public long count() {
        return repository.count();
    }
}
