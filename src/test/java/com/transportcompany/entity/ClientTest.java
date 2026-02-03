package com.transportcompany.entity;

import com.transportcompany.enums.TransportType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    private Client client;

    @BeforeEach
    void setUp() {
        client = new Client("Test Client", "John Doe", "+359888123456", "test@example.com");
    }

    @Test
    void testDefaultConstructor() {
        Client emptyClient = new Client();
        assertNull(emptyClient.getName());
        assertNotNull(emptyClient.getTransports());
    }

    @Test
    void testNameOnlyConstructor() {
        Client simpleClient = new Client("Simple Client");
        assertEquals("Simple Client", simpleClient.getName());
        assertNull(simpleClient.getContactPerson());
    }

    @Test
    void testFullConstructor() {
        assertEquals("Test Client", client.getName());
        assertEquals("John Doe", client.getContactPerson());
        assertEquals("+359888123456", client.getPhone());
        assertEquals("test@example.com", client.getEmail());
    }

    @Test
    void testSettersAndGetters() {
        client.setId(1L);
        client.setAddress("123 Test Street");

        assertEquals(1L, client.getId());
        assertEquals("123 Test Street", client.getAddress());
    }

    @Test
    void testHasPaidAllTransports_NoTransports() {
        assertTrue(client.hasPaidAllTransports());
    }

    @Test
    void testHasPaidAllTransports_AllPaid() {
        Transport t1 = createTransport(true);
        Transport t2 = createTransport(true);

        client.getTransports().add(t1);
        client.getTransports().add(t2);

        assertTrue(client.hasPaidAllTransports());
    }

    @Test
    void testHasPaidAllTransports_SomeUnpaid() {
        Transport t1 = createTransport(true);
        Transport t2 = createTransport(false);

        client.getTransports().add(t1);
        client.getTransports().add(t2);

        assertFalse(client.hasPaidAllTransports());
    }

    @Test
    void testHasPaidAllTransports_AllUnpaid() {
        Transport t1 = createTransport(false);
        Transport t2 = createTransport(false);

        client.getTransports().add(t1);
        client.getTransports().add(t2);

        assertFalse(client.hasPaidAllTransports());
    }

    @Test
    void testGetUnpaidTransportsCount_NoTransports() {
        assertEquals(0, client.getUnpaidTransportsCount());
    }

    @Test
    void testGetUnpaidTransportsCount_AllPaid() {
        client.getTransports().add(createTransport(true));
        client.getTransports().add(createTransport(true));

        assertEquals(0, client.getUnpaidTransportsCount());
    }

    @Test
    void testGetUnpaidTransportsCount_SomeUnpaid() {
        client.getTransports().add(createTransport(true));
        client.getTransports().add(createTransport(false));
        client.getTransports().add(createTransport(false));

        assertEquals(2, client.getUnpaidTransportsCount());
    }

    @Test
    void testCompanyAssociation() {
        TransportCompany company = new TransportCompany("Test Company", "Address");
        client.setCompany(company);

        assertEquals(company, client.getCompany());
    }

    @Test
    void testSetTransports() {
        ArrayList<Transport> transports = new ArrayList<>();
        transports.add(createTransport(true));

        client.setTransports(transports);

        assertEquals(1, client.getTransports().size());
    }

    @Test
    void testToString() {
        client.setId(1L);
        String result = client.toString();

        assertTrue(result.contains("Test Client"));
        assertTrue(result.contains("John Doe"));
        assertTrue(result.contains("test@example.com"));
    }

    private Transport createTransport(boolean paid) {
        Transport transport = new Transport(
                "Start", "End",
                LocalDate.now(), LocalDate.now().plusDays(1),
                TransportType.GOODS, new BigDecimal("100.00")
        );
        transport.setPaid(paid);
        return transport;
    }
}
