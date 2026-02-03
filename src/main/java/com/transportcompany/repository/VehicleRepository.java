package com.transportcompany.repository;

import com.transportcompany.entity.Vehicle;
import com.transportcompany.enums.VehicleType;
import org.hibernate.Session;
import java.util.List;
import java.util.Optional;

public class VehicleRepository extends GenericRepository<Vehicle, Long> {

    public VehicleRepository() {
        super(Vehicle.class);
    }

    public List<Vehicle> findByCompanyId(Long companyId) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Vehicle WHERE company.id = :companyId", Vehicle.class)
                    .setParameter("companyId", companyId)
                    .list();
        }
    }

    public Optional<Vehicle> findByRegistrationNumber(String registrationNumber) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Vehicle WHERE registrationNumber = :regNum", Vehicle.class)
                    .setParameter("regNum", registrationNumber)
                    .uniqueResultOptional();
        }
    }

    public List<Vehicle> findByVehicleType(Long companyId, VehicleType vehicleType) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "FROM Vehicle WHERE company.id = :companyId AND vehicleType = :type", Vehicle.class)
                    .setParameter("companyId", companyId)
                    .setParameter("type", vehicleType)
                    .list();
        }
    }

    public Vehicle findByIdWithTransports(Long id) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "SELECT DISTINCT v FROM Vehicle v LEFT JOIN FETCH v.transports WHERE v.id = :id",
                    Vehicle.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }
}
