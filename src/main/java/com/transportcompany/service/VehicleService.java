package com.transportcompany.service;

import com.transportcompany.entity.TransportCompany;
import com.transportcompany.entity.Vehicle;
import com.transportcompany.enums.VehicleType;
import com.transportcompany.repository.TransportCompanyRepository;
import com.transportcompany.repository.VehicleRepository;
import com.transportcompany.validation.ValidationException;
import com.transportcompany.validation.ValidationUtil;
import java.util.List;
import java.util.Optional;

public class VehicleService {

    private final VehicleRepository repository;
    private final TransportCompanyRepository companyRepository;

    public VehicleService() {
        this.repository = new VehicleRepository();
        this.companyRepository = new TransportCompanyRepository();
    }

    public Vehicle create(Long companyId, String registrationNumber, VehicleType vehicleType,
                          String brand, String model, Integer year, Integer capacity) {
        // Check if vehicle with this registration already exists
        if (repository.findByRegistrationNumber(registrationNumber).isPresent()) {
            throw new ValidationException("Vehicle with registration number " + registrationNumber + " already exists");
        }

        TransportCompany company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company not found with id: " + companyId));

        Vehicle vehicle = new Vehicle(registrationNumber, vehicleType, brand, model, year);
        vehicle.setCapacity(capacity);
        vehicle.setCompany(company);

        ValidationUtil.validate(vehicle);
        return repository.save(vehicle);
    }

    public Vehicle update(Vehicle vehicle) {
        ValidationUtil.validate(vehicle);
        return repository.update(vehicle);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<Vehicle> findById(Long id) {
        return repository.findById(id);
    }

    public Vehicle findByIdWithTransports(Long id) {
        return repository.findByIdWithTransports(id);
    }

    public List<Vehicle> findByCompanyId(Long companyId) {
        return repository.findByCompanyId(companyId);
    }

    public Optional<Vehicle> findByRegistrationNumber(String registrationNumber) {
        return repository.findByRegistrationNumber(registrationNumber);
    }

    public List<Vehicle> findByVehicleType(Long companyId, VehicleType vehicleType) {
        return repository.findByVehicleType(companyId, vehicleType);
    }

    public long count() {
        return repository.count();
    }
}
