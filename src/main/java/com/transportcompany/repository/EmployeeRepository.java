package com.transportcompany.repository;

import com.transportcompany.entity.Employee;
import com.transportcompany.enums.DriverQualification;
import org.hibernate.Session;
import java.math.BigDecimal;
import java.util.List;

public class EmployeeRepository extends GenericRepository<Employee, Long> {

    public EmployeeRepository() {
        super(Employee.class);
    }

    public List<Employee> findByCompanyId(Long companyId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Employee WHERE company.id = :companyId", Employee.class)
                    .setParameter("companyId", companyId)
                    .list();
        }
    }

    public List<Employee> findByCompanyIdSortedBySalary(Long companyId, boolean ascending) {
        try (Session session = getSession()) {
            String order = ascending ? "ASC" : "DESC";
            return session.createQuery(
                    "FROM Employee WHERE company.id = :companyId ORDER BY salary " + order, Employee.class)
                    .setParameter("companyId", companyId)
                    .list();
        }
    }

    public List<Employee> findByQualification(Long companyId, DriverQualification qualification) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "SELECT e FROM Employee e JOIN e.qualifications q " +
                    "WHERE e.company.id = :companyId AND q = :qualification", Employee.class)
                    .setParameter("companyId", companyId)
                    .setParameter("qualification", qualification)
                    .list();
        }
    }

    public List<Employee> findBySalaryRange(Long companyId, BigDecimal minSalary, BigDecimal maxSalary) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Employee WHERE company.id = :companyId AND salary BETWEEN :minSalary AND :maxSalary",
                    Employee.class)
                    .setParameter("companyId", companyId)
                    .setParameter("minSalary", minSalary)
                    .setParameter("maxSalary", maxSalary)
                    .list();
        }
    }

    public Employee findByIdWithTransports(Long id) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "SELECT DISTINCT e FROM Employee e LEFT JOIN FETCH e.transports WHERE e.id = :id",
                    Employee.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }
}
