package com.transportcompany.repository;

import com.transportcompany.entity.Transport;
import com.transportcompany.enums.TransportType;
import org.hibernate.Session;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TransportRepository extends GenericRepository<Transport, Long> {

    public TransportRepository() {
        super(Transport.class);
    }

    public List<Transport> findByCompanyId(Long companyId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Transport WHERE company.id = :companyId", Transport.class)
                    .setParameter("companyId", companyId)
                    .list();
        }
    }

    public List<Transport> findByCompanyIdSortedByDestination(Long companyId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Transport WHERE company.id = :companyId ORDER BY endPoint ASC", Transport.class)
                    .setParameter("companyId", companyId)
                    .list();
        }
    }

    public List<Transport> findByDriverId(Long driverId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Transport WHERE driver.id = :driverId", Transport.class)
                    .setParameter("driverId", driverId)
                    .list();
        }
    }

    public List<Transport> findByClientId(Long clientId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Transport WHERE client.id = :clientId", Transport.class)
                    .setParameter("clientId", clientId)
                    .list();
        }
    }

    public List<Transport> findUnpaidByClientId(Long clientId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Transport WHERE client.id = :clientId AND paid = false", Transport.class)
                    .setParameter("clientId", clientId)
                    .list();
        }
    }

    public List<Transport> findByDateRange(Long companyId, LocalDate startDate, LocalDate endDate) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Transport WHERE company.id = :companyId " +
                    "AND departureDate >= :startDate AND arrivalDate <= :endDate", Transport.class)
                    .setParameter("companyId", companyId)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .list();
        }
    }

    public List<Transport> findByDestination(Long companyId, String destination) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Transport WHERE company.id = :companyId " +
                    "AND (LOWER(startPoint) LIKE LOWER(:dest) OR LOWER(endPoint) LIKE LOWER(:dest))",
                    Transport.class)
                    .setParameter("companyId", companyId)
                    .setParameter("dest", "%" + destination + "%")
                    .list();
        }
    }

    public List<Transport> findByTransportType(Long companyId, TransportType type) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Transport WHERE company.id = :companyId AND transportType = :type", Transport.class)
                    .setParameter("companyId", companyId)
                    .setParameter("type", type)
                    .list();
        }
    }

    public BigDecimal getTotalRevenueByCompany(Long companyId) {
        try (Session session = getSession()) {
            BigDecimal result = session.createQuery(
                    "SELECT COALESCE(SUM(price), 0) FROM Transport WHERE company.id = :companyId AND paid = true",
                    BigDecimal.class)
                    .setParameter("companyId", companyId)
                    .uniqueResult();
            return result != null ? result : BigDecimal.ZERO;
        }
    }

    public BigDecimal getTotalRevenueByDriver(Long driverId) {
        try (Session session = getSession()) {
            BigDecimal result = session.createQuery(
                    "SELECT COALESCE(SUM(price), 0) FROM Transport WHERE driver.id = :driverId AND paid = true",
                    BigDecimal.class)
                    .setParameter("driverId", driverId)
                    .uniqueResult();
            return result != null ? result : BigDecimal.ZERO;
        }
    }

    public BigDecimal getRevenueByDateRange(Long companyId, LocalDate startDate, LocalDate endDate) {
        try (Session session = getSession()) {
            BigDecimal result = session.createQuery(
                    "SELECT COALESCE(SUM(price), 0) FROM Transport " +
                    "WHERE company.id = :companyId AND paid = true " +
                    "AND departureDate >= :startDate AND arrivalDate <= :endDate", BigDecimal.class)
                    .setParameter("companyId", companyId)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .uniqueResult();
            return result != null ? result : BigDecimal.ZERO;
        }
    }

    public long countByCompany(Long companyId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "SELECT COUNT(*) FROM Transport WHERE company.id = :companyId", Long.class)
                    .setParameter("companyId", companyId)
                    .uniqueResult();
        }
    }

    public long countByDriver(Long driverId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "SELECT COUNT(*) FROM Transport WHERE driver.id = :driverId", Long.class)
                    .setParameter("driverId", driverId)
                    .uniqueResult();
        }
    }
}
