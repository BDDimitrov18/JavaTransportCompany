package com.transportcompany.entity;

import com.transportcompany.enums.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle("CB1234AB", VehicleType.TRUCK, "Mercedes", "Actros", 2022);
    }

    @Test
    void testDefaultConstructor() {
        Vehicle emptyVehicle = new Vehicle();
        assertNull(emptyVehicle.getRegistrationNumber());
        assertNotNull(emptyVehicle.getTransports());
    }

    @Test
    void testSimpleConstructor() {
        Vehicle simpleVehicle = new Vehicle("CB5678CD", VehicleType.BUS);
        assertEquals("CB5678CD", simpleVehicle.getRegistrationNumber());
        assertEquals(VehicleType.BUS, simpleVehicle.getVehicleType());
        assertNull(simpleVehicle.getBrand());
    }

    @Test
    void testFullConstructor() {
        assertEquals("CB1234AB", vehicle.getRegistrationNumber());
        assertEquals(VehicleType.TRUCK, vehicle.getVehicleType());
        assertEquals("Mercedes", vehicle.getBrand());
        assertEquals("Actros", vehicle.getModel());
        assertEquals(2022, vehicle.getYear());
    }

    @Test
    void testSettersAndGetters() {
        vehicle.setId(1L);
        vehicle.setCapacity(20);

        assertEquals(1L, vehicle.getId());
        assertEquals(20, vehicle.getCapacity());
    }

    @Test
    void testGetFullDescription_AllFields() {
        String description = vehicle.getFullDescription();

        assertTrue(description.contains("Камион"));
        assertTrue(description.contains("Mercedes"));
        assertTrue(description.contains("Actros"));
        assertTrue(description.contains("CB1234AB"));
    }

    @Test
    void testGetFullDescription_OnlyTypeAndRegistration() {
        Vehicle simpleVehicle = new Vehicle("CB5678CD", VehicleType.BUS);
        String description = simpleVehicle.getFullDescription();

        assertTrue(description.contains("Автобус"));
        assertTrue(description.contains("CB5678CD"));
        assertFalse(description.contains("null"));
    }

    @Test
    void testGetFullDescription_WithBrandOnly() {
        Vehicle vehicleWithBrand = new Vehicle("CB5678CD", VehicleType.VAN);
        vehicleWithBrand.setBrand("Ford");
        String description = vehicleWithBrand.getFullDescription();

        assertTrue(description.contains("Бус"));
        assertTrue(description.contains("Ford"));
        assertTrue(description.contains("CB5678CD"));
    }

    @Test
    void testVehicleTypes() {
        vehicle.setVehicleType(VehicleType.TANKER);
        assertEquals(VehicleType.TANKER, vehicle.getVehicleType());

        vehicle.setVehicleType(VehicleType.CAR);
        assertEquals(VehicleType.CAR, vehicle.getVehicleType());
    }

    @Test
    void testCompanyAssociation() {
        TransportCompany company = new TransportCompany("Test Company", "Address");
        vehicle.setCompany(company);

        assertEquals(company, vehicle.getCompany());
    }

    @Test
    void testToString() {
        vehicle.setId(1L);
        String result = vehicle.toString();

        assertTrue(result.contains("CB1234AB"));
        assertTrue(result.contains("TRUCK"));
        assertTrue(result.contains("Mercedes"));
        assertTrue(result.contains("Actros"));
    }
}
