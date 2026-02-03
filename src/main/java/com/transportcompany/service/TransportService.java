package com.transportcompany.service;

import com.transportcompany.entity.*;
import com.transportcompany.enums.TransportType;
import com.transportcompany.repository.*;
import com.transportcompany.validation.ValidationException;
import com.transportcompany.validation.ValidationUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TransportService {

    private final TransportRepository repository;
    private final TransportCompanyRepository companyRepository;
    private final EmployeeRepository employeeRepository;
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;

    public TransportService() {
        this.repository = new TransportRepository();
        this.companyRepository = new TransportCompanyRepository();
        this.employeeRepository = new EmployeeRepository();
        this.vehicleRepository = new VehicleRepository();
        this.clientRepository = new ClientRepository();
    }

    public Transport create(Long companyId, Long driverId, Long vehicleId, Long clientId,
                            String startPoint, String endPoint, LocalDate departureDate,
                            LocalDate arrivalDate, TransportType transportType, BigDecimal price,
                            String cargoDescription, Double cargoWeight, Integer passengerCount) {

        // Validate dates
        if (arrivalDate.isBefore(departureDate)) {
            throw new ValidationException("Arrival date cannot be before departure date");
        }

        TransportCompany company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company not found with id: " + companyId));

        Employee driver = employeeRepository.findById(driverId)
                .orElseThrow(() -> new ValidationException("Driver not found with id: " + driverId));

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ValidationException("Vehicle not found with id: " + vehicleId));

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new ValidationException("Client not found with id: " + clientId));

        Transport transport = new Transport(startPoint, endPoint, departureDate, arrivalDate, transportType, price);
        transport.setCompany(company);
        transport.setDriver(driver);
        transport.setVehicle(vehicle);
        transport.setClient(client);
        transport.setCargoDescription(cargoDescription);
        transport.setCargoWeight(cargoWeight);
        transport.setPassengerCount(passengerCount);

        ValidationUtil.validate(transport);
        return repository.save(transport);
    }

    public Transport update(Transport transport) {
        ValidationUtil.validate(transport);
        return repository.update(transport);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<Transport> findById(Long id) {
        return repository.findById(id);
    }

    public List<Transport> findByCompanyId(Long companyId) {
        return repository.findByCompanyId(companyId);
    }

    public List<Transport> findByCompanyIdSortedByDestination(Long companyId) {
        return repository.findByCompanyIdSortedByDestination(companyId);
    }

    public List<Transport> findByDriverId(Long driverId) {
        return repository.findByDriverId(driverId);
    }

    public List<Transport> findByClientId(Long clientId) {
        return repository.findByClientId(clientId);
    }

    public List<Transport> findUnpaidByClientId(Long clientId) {
        return repository.findUnpaidByClientId(clientId);
    }

    public List<Transport> findByDateRange(Long companyId, LocalDate startDate, LocalDate endDate) {
        return repository.findByDateRange(companyId, startDate, endDate);
    }

    public List<Transport> findByDestination(Long companyId, String destination) {
        return repository.findByDestination(companyId, destination);
    }

    public List<Transport> findByTransportType(Long companyId, TransportType type) {
        return repository.findByTransportType(companyId, type);
    }

    public void markAsPaid(Long transportId) {
        Optional<Transport> transportOpt = repository.findById(transportId);
        if (transportOpt.isPresent()) {
            Transport transport = transportOpt.get();
            transport.setPaid(true);
            repository.update(transport);

            // Update company revenue
            TransportCompany company = transport.getCompany();
            if (company != null) {
                company.setRevenue(company.getRevenue().add(transport.getPrice()));
                companyRepository.update(company);
            }
        }
    }

    public void markAsUnpaid(Long transportId) {
        Optional<Transport> transportOpt = repository.findById(transportId);
        if (transportOpt.isPresent()) {
            Transport transport = transportOpt.get();
            if (transport.isPaid()) {
                transport.setPaid(false);
                repository.update(transport);

                // Update company revenue
                TransportCompany company = transport.getCompany();
                if (company != null) {
                    company.setRevenue(company.getRevenue().subtract(transport.getPrice()));
                    companyRepository.update(company);
                }
            }
        }
    }

    // Reporting methods
    public BigDecimal getTotalRevenueByCompany(Long companyId) {
        return repository.getTotalRevenueByCompany(companyId);
    }

    public BigDecimal getTotalRevenueByDriver(Long driverId) {
        return repository.getTotalRevenueByDriver(driverId);
    }

    public BigDecimal getRevenueByDateRange(Long companyId, LocalDate startDate, LocalDate endDate) {
        return repository.getRevenueByDateRange(companyId, startDate, endDate);
    }

    public long countByCompany(Long companyId) {
        return repository.countByCompany(companyId);
    }

    public long countByDriver(Long driverId) {
        return repository.countByDriver(driverId);
    }

    public long count() {
        return repository.count();
    }
}
