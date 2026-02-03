package com.transportcompany.entity;

import com.transportcompany.enums.TransportType;
import com.transportcompany.enums.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransportCompanyTest {

    private TransportCompany company;

    @BeforeEach
    void setUp() {
        company = new TransportCompany("Test Transport Ltd", "123 Main Street, Sofia");
    }

    @Test
    void testDefaultConstructor() {
        TransportCompany emptyCompany = new TransportCompany();
        assertNull(emptyCompany.getName());
        assertEquals(BigDecimal.ZERO, emptyCompany.getRevenue());
        assertNotNull(emptyCompany.getEmployees());
        assertNotNull(emptyCompany.getVehicles());
        assertNotNull(emptyCompany.getClients());
        assertNotNull(emptyCompany.getTransports());
    }

    @Test
    void testFullConstructor() {
        assertEquals("Test Transport Ltd", company.getName());
        assertEquals("123 Main Street, Sofia", company.getAddress());
        assertEquals(BigDecimal.ZERO, company.getRevenue());
    }

    @Test
    void testSettersAndGetters() {
        company.setId(1L);
        company.setRevenue(new BigDecimal("50000.00"));

        assertEquals(1L, company.getId());
        assertEquals(new BigDecimal("50000.00"), company.getRevenue());
    }

    @Test
    void testAddEmployee() {
        Employee employee = new Employee("Ivan", "Petrov", new BigDecimal("2000"));

        company.addEmployee(employee);

        assertEquals(1, company.getEmployees().size());
        assertTrue(company.getEmployees().contains(employee));
        assertEquals(company, employee.getCompany());
    }

    @Test
    void testAddMultipleEmployees() {
        Employee emp1 = new Employee("Ivan", "Petrov", new BigDecimal("2000"));
        Employee emp2 = new Employee("Maria", "Ivanova", new BigDecimal("2500"));

        company.addEmployee(emp1);
        company.addEmployee(emp2);

        assertEquals(2, company.getEmployees().size());
    }

    @Test
    void testRemoveEmployee() {
        Employee employee = new Employee("Ivan", "Petrov", new BigDecimal("2000"));
        company.addEmployee(employee);

        company.removeEmployee(employee);

        assertEquals(0, company.getEmployees().size());
        assertNull(employee.getCompany());
    }

    @Test
    void testAddVehicle() {
        Vehicle vehicle = new Vehicle("CB1234AB", VehicleType.TRUCK);

        company.addVehicle(vehicle);

        assertEquals(1, company.getVehicles().size());
        assertTrue(company.getVehicles().contains(vehicle));
        assertEquals(company, vehicle.getCompany());
    }

    @Test
    void testAddMultipleVehicles() {
        Vehicle v1 = new Vehicle("CB1234AB", VehicleType.TRUCK);
        Vehicle v2 = new Vehicle("CB5678CD", VehicleType.BUS);

        company.addVehicle(v1);
        company.addVehicle(v2);

        assertEquals(2, company.getVehicles().size());
    }

    @Test
    void testRemoveVehicle() {
        Vehicle vehicle = new Vehicle("CB1234AB", VehicleType.TRUCK);
        company.addVehicle(vehicle);

        company.removeVehicle(vehicle);

        assertEquals(0, company.getVehicles().size());
        assertNull(vehicle.getCompany());
    }

    @Test
    void testAddClient() {
        Client client = new Client("Client Corp");

        company.addClient(client);

        assertEquals(1, company.getClients().size());
        assertTrue(company.getClients().contains(client));
        assertEquals(company, client.getCompany());
    }

    @Test
    void testAddMultipleClients() {
        Client c1 = new Client("Client Corp");
        Client c2 = new Client("Another Client");

        company.addClient(c1);
        company.addClient(c2);

        assertEquals(2, company.getClients().size());
    }

    @Test
    void testRemoveClient() {
        Client client = new Client("Client Corp");
        company.addClient(client);

        company.removeClient(client);

        assertEquals(0, company.getClients().size());
        assertNull(client.getCompany());
    }

    @Test
    void testAddTransport() {
        Transport transport = new Transport(
                "Sofia", "Varna",
                LocalDate.now(), LocalDate.now().plusDays(1),
                TransportType.GOODS, new BigDecimal("500")
        );

        company.addTransport(transport);

        assertEquals(1, company.getTransports().size());
        assertTrue(company.getTransports().contains(transport));
        assertEquals(company, transport.getCompany());
    }

    @Test
    void testAddMultipleTransports() {
        Transport t1 = new Transport("Sofia", "Varna", LocalDate.now(), LocalDate.now().plusDays(1),
                TransportType.GOODS, new BigDecimal("500"));
        Transport t2 = new Transport("Plovdiv", "Burgas", LocalDate.now(), LocalDate.now().plusDays(1),
                TransportType.PASSENGERS, new BigDecimal("300"));

        company.addTransport(t1);
        company.addTransport(t2);

        assertEquals(2, company.getTransports().size());
    }

    @Test
    void testRemoveTransport() {
        Transport transport = new Transport(
                "Sofia", "Varna",
                LocalDate.now(), LocalDate.now().plusDays(1),
                TransportType.GOODS, new BigDecimal("500")
        );
        company.addTransport(transport);

        company.removeTransport(transport);

        assertEquals(0, company.getTransports().size());
        assertNull(transport.getCompany());
    }

    @Test
    void testSetCollections() {
        java.util.List<Employee> employees = new java.util.ArrayList<>();
        employees.add(new Employee("Test", "Employee", new BigDecimal("1000")));

        company.setEmployees(employees);

        assertEquals(1, company.getEmployees().size());
    }

    @Test
    void testToString() {
        company.setId(1L);
        company.setRevenue(new BigDecimal("100000"));
        String result = company.toString();

        assertTrue(result.contains("Test Transport Ltd"));
        assertTrue(result.contains("123 Main Street, Sofia"));
        assertTrue(result.contains("100000"));
    }

    @Test
    void testNameUpdate() {
        company.setName("New Company Name");
        assertEquals("New Company Name", company.getName());
    }

    @Test
    void testAddressUpdate() {
        company.setAddress("456 New Street");
        assertEquals("456 New Street", company.getAddress());
    }
}
