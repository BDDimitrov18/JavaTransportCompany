package com.transportcompany.service;

import com.transportcompany.entity.Employee;
import com.transportcompany.entity.TransportCompany;
import com.transportcompany.enums.DriverQualification;
import com.transportcompany.repository.EmployeeRepository;
import com.transportcompany.repository.TransportCompanyRepository;
import com.transportcompany.validation.ValidationException;
import com.transportcompany.validation.ValidationUtil;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class EmployeeService {

    private final EmployeeRepository repository;
    private final TransportCompanyRepository companyRepository;

    public EmployeeService() {
        this.repository = new EmployeeRepository();
        this.companyRepository = new TransportCompanyRepository();
    }

    public Employee create(Long companyId, String firstName, String lastName, BigDecimal salary,
                           Set<DriverQualification> qualifications) {
        TransportCompany company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ValidationException("Company not found with id: " + companyId));

        Employee employee = new Employee(firstName, lastName, salary);
        employee.setQualifications(qualifications);
        employee.setCompany(company);

        ValidationUtil.validate(employee);
        return repository.save(employee);
    }

    public Employee update(Employee employee) {
        ValidationUtil.validate(employee);
        return repository.update(employee);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<Employee> findById(Long id) {
        return repository.findById(id);
    }

    public Employee findByIdWithTransports(Long id) {
        return repository.findByIdWithTransports(id);
    }

    public List<Employee> findByCompanyId(Long companyId) {
        return repository.findByCompanyId(companyId);
    }

    public List<Employee> findByCompanyIdSortedBySalary(Long companyId, boolean ascending) {
        return repository.findByCompanyIdSortedBySalary(companyId, ascending);
    }

    public List<Employee> findByQualification(Long companyId, DriverQualification qualification) {
        return repository.findByQualification(companyId, qualification);
    }

    public List<Employee> findBySalaryRange(Long companyId, BigDecimal minSalary, BigDecimal maxSalary) {
        return repository.findBySalaryRange(companyId, minSalary, maxSalary);
    }

    public void addQualification(Long employeeId, DriverQualification qualification) {
        Optional<Employee> employeeOpt = repository.findById(employeeId);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            employee.addQualification(qualification);
            repository.update(employee);
        }
    }

    public void removeQualification(Long employeeId, DriverQualification qualification) {
        Optional<Employee> employeeOpt = repository.findById(employeeId);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();
            employee.removeQualification(qualification);
            repository.update(employee);
        }
    }

    public long count() {
        return repository.count();
    }
}
