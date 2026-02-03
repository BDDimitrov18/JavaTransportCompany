package com.transportcompany.repository;

import com.transportcompany.entity.TransportCompany;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

public class TransportCompanyRepository extends GenericRepository<TransportCompany, Long> {

    public TransportCompanyRepository() {
        super(TransportCompany.class);
    }

    public Optional<TransportCompany> findByName(String name) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM TransportCompany WHERE name = :name", TransportCompany.class)
                    .setParameter("name", name)
                    .uniqueResultOptional();
        }
    }

    public List<TransportCompany> findAllSortedByName() {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM TransportCompany ORDER BY name ASC", TransportCompany.class)
                    .list();
        }
    }

    public List<TransportCompany> findAllSortedByRevenue() {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM TransportCompany ORDER BY revenue DESC", TransportCompany.class)
                    .list();
        }
    }

    public TransportCompany findByIdWithDetails(Long id) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "SELECT DISTINCT c FROM TransportCompany c " +
                    "LEFT JOIN FETCH c.employees " +
                    "LEFT JOIN FETCH c.vehicles " +
                    "LEFT JOIN FETCH c.clients " +
                    "WHERE c.id = :id", TransportCompany.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }
}
