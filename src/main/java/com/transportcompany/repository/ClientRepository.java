package com.transportcompany.repository;

import com.transportcompany.entity.Client;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

public class ClientRepository extends GenericRepository<Client, Long> {

    public ClientRepository() {
        super(Client.class);
    }

    public List<Client> findByCompanyId(Long companyId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Client WHERE company.id = :companyId", Client.class)
                    .setParameter("companyId", companyId)
                    .list();
        }
    }

    public Optional<Client> findByNameAndCompany(String name, Long companyId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Client WHERE name = :name AND company.id = :companyId", Client.class)
                    .setParameter("name", name)
                    .setParameter("companyId", companyId)
                    .uniqueResultOptional();
        }
    }

    public List<Client> findClientsWithUnpaidTransports(Long companyId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "SELECT DISTINCT c FROM Client c JOIN c.transports t " +
                    "WHERE c.company.id = :companyId AND t.paid = false", Client.class)
                    .setParameter("companyId", companyId)
                    .list();
        }
    }

    public Client findByIdWithTransports(Long id) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "SELECT DISTINCT c FROM Client c LEFT JOIN FETCH c.transports WHERE c.id = :id",
                    Client.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }
}
