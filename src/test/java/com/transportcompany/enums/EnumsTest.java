package com.transportcompany.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumsTest {

    @Test
    void testDriverQualificationValues() {
        assertEquals(6, DriverQualification.values().length);
        assertNotNull(DriverQualification.STANDARD);
        assertNotNull(DriverQualification.HAZARDOUS_MATERIALS);
        assertNotNull(DriverQualification.FLAMMABLE_MATERIALS);
        assertNotNull(DriverQualification.PASSENGER_TRANSPORT);
        assertNotNull(DriverQualification.HEAVY_CARGO);
        assertNotNull(DriverQualification.SPECIAL_CARGO);
    }

    @Test
    void testDriverQualificationDisplayNames() {
        assertEquals("Стандартна правоспособност", DriverQualification.STANDARD.getDisplayName());
        assertEquals("Превоз на опасни товари", DriverQualification.HAZARDOUS_MATERIALS.getDisplayName());
        assertEquals("Превоз на леснозапалими товари", DriverQualification.FLAMMABLE_MATERIALS.getDisplayName());
        assertEquals("Превоз на над 12 пътника", DriverQualification.PASSENGER_TRANSPORT.getDisplayName());
        assertEquals("Превоз на тежки товари", DriverQualification.HEAVY_CARGO.getDisplayName());
        assertEquals("Превоз на специални товари", DriverQualification.SPECIAL_CARGO.getDisplayName());
    }

    @Test
    void testVehicleTypeValues() {
        assertEquals(5, VehicleType.values().length);
        assertNotNull(VehicleType.BUS);
        assertNotNull(VehicleType.TRUCK);
        assertNotNull(VehicleType.TANKER);
        assertNotNull(VehicleType.VAN);
        assertNotNull(VehicleType.CAR);
    }

    @Test
    void testVehicleTypeDisplayNames() {
        assertEquals("Автобус", VehicleType.BUS.getDisplayName());
        assertEquals("Камион", VehicleType.TRUCK.getDisplayName());
        assertEquals("Цистерна", VehicleType.TANKER.getDisplayName());
        assertEquals("Бус", VehicleType.VAN.getDisplayName());
        assertEquals("Лек автомобил", VehicleType.CAR.getDisplayName());
    }

    @Test
    void testTransportTypeValues() {
        assertEquals(2, TransportType.values().length);
        assertNotNull(TransportType.PASSENGERS);
        assertNotNull(TransportType.GOODS);
    }

    @Test
    void testTransportTypeDisplayNames() {
        assertEquals("Пътници", TransportType.PASSENGERS.getDisplayName());
        assertEquals("Товари", TransportType.GOODS.getDisplayName());
    }

    @Test
    void testDriverQualificationValueOf() {
        assertEquals(DriverQualification.STANDARD, DriverQualification.valueOf("STANDARD"));
        assertEquals(DriverQualification.HEAVY_CARGO, DriverQualification.valueOf("HEAVY_CARGO"));
    }

    @Test
    void testVehicleTypeValueOf() {
        assertEquals(VehicleType.BUS, VehicleType.valueOf("BUS"));
        assertEquals(VehicleType.TRUCK, VehicleType.valueOf("TRUCK"));
    }

    @Test
    void testTransportTypeValueOf() {
        assertEquals(TransportType.PASSENGERS, TransportType.valueOf("PASSENGERS"));
        assertEquals(TransportType.GOODS, TransportType.valueOf("GOODS"));
    }
}
