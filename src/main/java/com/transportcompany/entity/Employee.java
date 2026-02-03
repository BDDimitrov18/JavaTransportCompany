package com.transportcompany.entity;

import com.transportcompany.enums.DriverQualification;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Column(nullable = false)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Column(nullable = false)
    private String lastName;

    @Column(unique = true)
    private String phone;

    @NotNull(message = "Salary is required")
    @Positive(message = "Salary must be positive")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;

    @ElementCollection(targetClass = DriverQualification.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "employee_qualifications", joinColumns = @JoinColumn(name = "employee_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "qualification")
    private Set<DriverQualification> qualifications = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private TransportCompany company;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<Transport> transports = new ArrayList<>();

    public Employee() {}

    public Employee(String firstName, String lastName, BigDecimal salary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public Set<DriverQualification> getQualifications() {
        return qualifications;
    }

    public void setQualifications(Set<DriverQualification> qualifications) {
        this.qualifications = qualifications;
    }

    public void addQualification(DriverQualification qualification) {
        this.qualifications.add(qualification);
    }

    public void removeQualification(DriverQualification qualification) {
        this.qualifications.remove(qualification);
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

    public int getTransportCount() {
        return transports.size();
    }

    public BigDecimal getTotalRevenue() {
        return transports.stream()
                .map(Transport::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + getFullName() + '\'' +
                ", salary=" + salary +
                ", qualifications=" + qualifications +
                '}';
    }
}
