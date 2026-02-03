package com.transportcompany.entity;

import com.transportcompany.enums.DriverQualification;
import com.transportcompany.enums.TransportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee("John", "Doe", new BigDecimal("2500.00"));
    }

    @Test
    void testDefaultConstructor() {
        Employee emptyEmployee = new Employee();
        assertNull(emptyEmployee.getFirstName());
        assertNotNull(emptyEmployee.getQualifications());
        assertNotNull(emptyEmployee.getTransports());
    }

    @Test
    void testFullConstructor() {
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals(new BigDecimal("2500.00"), employee.getSalary());
    }

    @Test
    void testGetFullName() {
        assertEquals("John Doe", employee.getFullName());
    }

    @Test
    void testSettersAndGetters() {
        employee.setId(1L);
        employee.setPhone("+359888123456");

        assertEquals(1L, employee.getId());
        assertEquals("+359888123456", employee.getPhone());
    }

    @Test
    void testAddQualification() {
        employee.addQualification(DriverQualification.STANDARD);
        employee.addQualification(DriverQualification.HAZARDOUS_MATERIALS);

        assertTrue(employee.getQualifications().contains(DriverQualification.STANDARD));
        assertTrue(employee.getQualifications().contains(DriverQualification.HAZARDOUS_MATERIALS));
        assertEquals(2, employee.getQualifications().size());
    }

    @Test
    void testAddDuplicateQualification() {
        employee.addQualification(DriverQualification.STANDARD);
        employee.addQualification(DriverQualification.STANDARD);

        assertEquals(1, employee.getQualifications().size());
    }

    @Test
    void testRemoveQualification() {
        employee.addQualification(DriverQualification.STANDARD);
        employee.addQualification(DriverQualification.HEAVY_CARGO);

        employee.removeQualification(DriverQualification.STANDARD);

        assertFalse(employee.getQualifications().contains(DriverQualification.STANDARD));
        assertTrue(employee.getQualifications().contains(DriverQualification.HEAVY_CARGO));
        assertEquals(1, employee.getQualifications().size());
    }

    @Test
    void testRemoveNonExistentQualification() {
        employee.addQualification(DriverQualification.STANDARD);
        employee.removeQualification(DriverQualification.HEAVY_CARGO);

        assertEquals(1, employee.getQualifications().size());
    }

    @Test
    void testGetTransportCount_NoTransports() {
        assertEquals(0, employee.getTransportCount());
    }

    @Test
    void testGetTransportCount_WithTransports() {
        employee.getTransports().add(createTransport(new BigDecimal("100")));
        employee.getTransports().add(createTransport(new BigDecimal("200")));
        employee.getTransports().add(createTransport(new BigDecimal("300")));

        assertEquals(3, employee.getTransportCount());
    }

    @Test
    void testGetTotalRevenue_NoTransports() {
        assertEquals(BigDecimal.ZERO, employee.getTotalRevenue());
    }

    @Test
    void testGetTotalRevenue_WithTransports() {
        employee.getTransports().add(createTransport(new BigDecimal("100.50")));
        employee.getTransports().add(createTransport(new BigDecimal("200.25")));
        employee.getTransports().add(createTransport(new BigDecimal("300.25")));

        assertEquals(new BigDecimal("601.00"), employee.getTotalRevenue());
    }

    @Test
    void testCompanyAssociation() {
        TransportCompany company = new TransportCompany("Test Company", "Address");
        employee.setCompany(company);

        assertEquals(company, employee.getCompany());
    }

    @Test
    void testToString() {
        employee.setId(1L);
        employee.addQualification(DriverQualification.STANDARD);
        String result = employee.toString();

        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("2500"));
    }

    private Transport createTransport(BigDecimal price) {
        return new Transport(
                "Start", "End",
                LocalDate.now(), LocalDate.now().plusDays(1),
                TransportType.GOODS, price
        );
    }
}
