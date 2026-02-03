package com.transportcompany.entity;

import com.transportcompany.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Registration number is required")
    @Column(nullable = false, unique = true)
    private String registrationNumber;

    @NotNull(message = "Vehicle type is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    private String brand;

    private String model;

    private Integer year;

    private Integer capacity; // seats for bus, tons for truck

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private TransportCompany company;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Transport> transports = new ArrayList<>();

    public Vehicle() {}

    public Vehicle(String registrationNumber, VehicleType vehicleType) {
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
    }

    public Vehicle(String registrationNumber, VehicleType vehicleType, String brand, String model, Integer year) {
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public TransportCompany getCompany() {
        return company;
    }

    public void setCompany(TransportCompany company) {
        this.company = company;
    }

    public List<Transport> getTransports() {
        return transports;
    }

    public void setTransports(List<Transport> transports) {
        this.transports = transports;
    }

    public String getFullDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(vehicleType.getDisplayName());
        if (brand != null) {
            sb.append(" ").append(brand);
        }
        if (model != null) {
            sb.append(" ").append(model);
        }
        sb.append(" (").append(registrationNumber).append(")");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", vehicleType=" + vehicleType +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}
