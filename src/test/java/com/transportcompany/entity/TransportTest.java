package com.transportcompany.entity;

import com.transportcompany.enums.TransportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransportTest {

    private Transport transport;

    @BeforeEach
    void setUp() {
        transport = new Transport(
                "Sofia", "Varna",
                LocalDate.of(2024, 1, 15),
                LocalDate.of(2024, 1, 16),
                TransportType.GOODS,
                new BigDecimal("500.00")
        );
    }

    @Test
    void testDefaultConstructor() {
        Transport emptyTransport = new Transport();
        assertNull(emptyTransport.getStartPoint());
        assertFalse(emptyTransport.isPaid());
    }

    @Test
    void testFullConstructor() {
        assertEquals("Sofia", transport.getStartPoint());
        assertEquals("Varna", transport.getEndPoint());
        assertEquals(LocalDate.of(2024, 1, 15), transport.getDepartureDate());
        assertEquals(LocalDate.of(2024, 1, 16), transport.getArrivalDate());
        assertEquals(TransportType.GOODS, transport.getTransportType());
        assertEquals(new BigDecimal("500.00"), transport.getPrice());
        assertFalse(transport.isPaid());
    }

    @Test
    void testGetDestination() {
        assertEquals("Sofia -> Varna", transport.getDestination());
    }

    @Test
    void testGetDestination_DifferentCities() {
        transport.setStartPoint("Plovdiv");
        transport.setEndPoint("Burgas");

        assertEquals("Plovdiv -> Burgas", transport.getDestination());
    }

    @Test
    void testSettersAndGetters() {
        transport.setId(1L);
        transport.setCargoDescription("Electronics");
        transport.setCargoWeight(1500.5);
        transport.setPassengerCount(null);

        assertEquals(1L, transport.getId());
        assertEquals("Electronics", transport.getCargoDescription());
        assertEquals(1500.5, transport.getCargoWeight());
        assertNull(transport.getPassengerCount());
    }

    @Test
    void testPaidStatus() {
        assertFalse(transport.isPaid());

        transport.setPaid(true);
        assertTrue(transport.isPaid());

        transport.setPaid(false);
        assertFalse(transport.isPaid());
    }

    @Test
    void testPassengerTransport() {
        Transport passengerTransport = new Transport(
                "Sofia", "Plovdiv",
                LocalDate.now(), LocalDate.now(),
                TransportType.PASSENGERS,
                new BigDecimal("50.00")
        );
        passengerTransport.setPassengerCount(45);

        assertEquals(TransportType.PASSENGERS, passengerTransport.getTransportType());
        assertEquals(45, passengerTransport.getPassengerCount());
    }

    @Test
    void testGoodsTransport() {
        transport.setCargoDescription("Furniture");
        transport.setCargoWeight(2500.0);

        assertEquals(TransportType.GOODS, transport.getTransportType());
        assertEquals("Furniture", transport.getCargoDescription());
        assertEquals(2500.0, transport.getCargoWeight());
    }

    @Test
    void testDriverAssociation() {
        Employee driver = new Employee("Ivan", "Petrov", new BigDecimal("2000"));
        transport.setDriver(driver);

        assertEquals(driver, transport.getDriver());
    }

    @Test
    void testVehicleAssociation() {
        Vehicle vehicle = new Vehicle("CB1234AB", com.transportcompany.enums.VehicleType.TRUCK);
        transport.setVehicle(vehicle);

        assertEquals(vehicle, transport.getVehicle());
    }

    @Test
    void testClientAssociation() {
        Client client = new Client("Test Client");
        transport.setClient(client);

        assertEquals(client, transport.getClient());
    }

    @Test
    void testCompanyAssociation() {
        TransportCompany company = new TransportCompany("Test Company", "Address");
        transport.setCompany(company);

        assertEquals(company, transport.getCompany());
    }

    @Test
    void testToString() {
        transport.setId(1L);
        transport.setPaid(true);
        String result = transport.toString();

        assertTrue(result.contains("Sofia -> Varna"));
        assertTrue(result.contains("500"));
        assertTrue(result.contains("paid=true"));
    }

    @Test
    void testPriceUpdate() {
        transport.setPrice(new BigDecimal("750.50"));
        assertEquals(new BigDecimal("750.50"), transport.getPrice());
    }

    @Test
    void testDateUpdate() {
        LocalDate newDeparture = LocalDate.of(2024, 2, 1);
        LocalDate newArrival = LocalDate.of(2024, 2, 3);

        transport.setDepartureDate(newDeparture);
        transport.setArrivalDate(newArrival);

        assertEquals(newDeparture, transport.getDepartureDate());
        assertEquals(newArrival, transport.getArrivalDate());
    }
}
