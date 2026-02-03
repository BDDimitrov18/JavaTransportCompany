package com.transportcompany.cli;

import com.transportcompany.entity.*;
import com.transportcompany.enums.TransportType;
import com.transportcompany.validation.ValidationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TransportMenuHandler {

    private final MenuContext ctx;

    public TransportMenuHandler(MenuContext ctx) {
        this.ctx = ctx;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Transports Menu ---");
            System.out.println("1. Add transport");
            System.out.println("2. List transports");
            System.out.println("3. Edit transport");
            System.out.println("4. Delete transport");
            System.out.println("5. Mark as paid/unpaid");
            System.out.println("6. Filter by destination");
            System.out.println("7. Filter by date range");
            System.out.println("8. Filter by type");
            System.out.println("0. Back");
            System.out.print("Choice: ");

            int choice = ctx.getInput().readInt();
            switch (choice) {
                case 1 -> addTransport();
                case 2 -> listTransports();
                case 3 -> editTransport();
                case 4 -> deleteTransport();
                case 5 -> togglePaidStatus();
                case 6 -> filterByDestination();
                case 7 -> filterByDateRange();
                case 8 -> filterByTransportType();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addTransport() {
        System.out.println("\n--- Add Transport ---");

        // Select driver
        List<Employee> employees = ctx.getEmployeeService().findByCompanyId(ctx.getCurrentCompanyId());
        if (employees.isEmpty()) {
            System.out.println("No employees found. Please add employees first.");
            return;
        }
        System.out.println("Select driver:");
        for (Employee e : employees) {
            System.out.println(e.getId() + ". " + e.getFullName());
        }
        System.out.print("Driver ID: ");
        Long driverId = ctx.getInput().readLong();

        // Select vehicle
        List<Vehicle> vehicles = ctx.getVehicleService().findByCompanyId(ctx.getCurrentCompanyId());
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found. Please add vehicles first.");
            return;
        }
        System.out.println("Select vehicle:");
        for (Vehicle v : vehicles) {
            System.out.println(v.getId() + ". " + v.getFullDescription());
        }
        System.out.print("Vehicle ID: ");
        Long vehicleId = ctx.getInput().readLong();

        // Select client
        List<Client> clients = ctx.getClientService().findByCompanyId(ctx.getCurrentCompanyId());
        if (clients.isEmpty()) {
            System.out.println("No clients found. Please add clients first.");
            return;
        }
        System.out.println("Select client:");
        for (Client c : clients) {
            System.out.println(c.getId() + ". " + c.getName());
        }
        System.out.print("Client ID: ");
        Long clientId = ctx.getInput().readLong();

        System.out.print("Start point: ");
        String startPoint = ctx.getInput().readLine();
        System.out.print("End point: ");
        String endPoint = ctx.getInput().readLine();
        System.out.print("Departure date (yyyy-MM-dd): ");
        LocalDate departureDate = ctx.getInput().readDate();
        System.out.print("Arrival date (yyyy-MM-dd): ");
        LocalDate arrivalDate = ctx.getInput().readDate();

        System.out.println("Transport type:");
        System.out.println("1. Passengers");
        System.out.println("2. Goods");
        System.out.print("Choice: ");
        int typeChoice = ctx.getInput().readInt();
        TransportType transportType = typeChoice == 1 ? TransportType.PASSENGERS : TransportType.GOODS;

        String cargoDescription = null;
        Double cargoWeight = null;
        Integer passengerCount = null;

        if (transportType == TransportType.GOODS) {
            System.out.print("Cargo description: ");
            cargoDescription = ctx.getInput().readLine();
            System.out.print("Cargo weight (kg): ");
            cargoWeight = ctx.getInput().readDouble();
        } else {
            System.out.print("Number of passengers: ");
            passengerCount = ctx.getInput().readIntOrNull();
        }

        System.out.print("Price: ");
        BigDecimal price = ctx.getInput().readBigDecimal();

        try {
            Transport transport = ctx.getTransportService().create(
                    ctx.getCurrentCompanyId(), driverId, vehicleId, clientId,
                    startPoint, endPoint, departureDate, arrivalDate, transportType, price,
                    cargoDescription, cargoWeight, passengerCount);
            System.out.println("Transport added! ID: " + transport.getId());
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listTransports() {
        System.out.println("Sort by: 1. Default | 2. Destination");
        int sortChoice = ctx.getInput().readInt();

        List<Transport> transports = sortChoice == 2
                ? ctx.getTransportService().findByCompanyIdSortedByDestination(ctx.getCurrentCompanyId())
                : ctx.getTransportService().findByCompanyId(ctx.getCurrentCompanyId());

        printTransportList(transports);
    }

    public void printTransportList(List<Transport> transports) {
        if (transports.isEmpty()) {
            System.out.println("No transports found.");
            return;
        }

        System.out.printf("%-5s %-25s %-12s %-12s %-10s %12s %-6s%n",
                "ID", "Destination", "Departure", "Arrival", "Type", "Price", "Paid");
        System.out.println("-".repeat(90));
        for (Transport t : transports) {
            System.out.printf("%-5d %-25s %-12s %-12s %-10s %12.2f %-6s%n",
                    t.getId(), truncate(t.getDestination(), 25),
                    t.getDepartureDate(), t.getArrivalDate(),
                    t.getTransportType().name().substring(0, Math.min(10, t.getTransportType().name().length())),
                    t.getPrice(), t.isPaid() ? "Yes" : "No");
        }
    }

    private String truncate(String s, int len) {
        return s.length() > len ? s.substring(0, len - 3) + "..." : s;
    }

    private void editTransport() {
        listTransports();
        System.out.print("Enter transport ID to edit: ");
        Long id = ctx.getInput().readLong();
        Optional<Transport> transOpt = ctx.getTransportService().findById(id);
        if (transOpt.isEmpty()) {
            System.out.println("Transport not found.");
            return;
        }

        Transport t = transOpt.get();
        System.out.print("New start point (Enter to keep '" + t.getStartPoint() + "'): ");
        String sp = ctx.getInput().readLine();
        if (!sp.isBlank()) t.setStartPoint(sp);

        System.out.print("New end point (Enter to keep '" + t.getEndPoint() + "'): ");
        String ep = ctx.getInput().readLine();
        if (!ep.isBlank()) t.setEndPoint(ep);

        System.out.print("New price (Enter to keep " + t.getPrice() + "): ");
        String priceStr = ctx.getInput().readLine();
        if (!priceStr.isBlank()) t.setPrice(new BigDecimal(priceStr));

        try {
            ctx.getTransportService().update(t);
            System.out.println("Transport updated.");
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteTransport() {
        listTransports();
        System.out.print("Enter transport ID to delete: ");
        Long id = ctx.getInput().readLong();
        ctx.getTransportService().delete(id);
        System.out.println("Transport deleted.");
    }

    private void togglePaidStatus() {
        listTransports();
        System.out.print("Enter transport ID: ");
        Long id = ctx.getInput().readLong();
        Optional<Transport> transOpt = ctx.getTransportService().findById(id);
        if (transOpt.isEmpty()) {
            System.out.println("Transport not found.");
            return;
        }

        Transport t = transOpt.get();
        if (t.isPaid()) {
            if (ctx.getInput().confirm("Mark as UNPAID?")) {
                ctx.getTransportService().markAsUnpaid(id);
                System.out.println("Transport marked as unpaid.");
            }
        } else {
            if (ctx.getInput().confirm("Mark as PAID?")) {
                ctx.getTransportService().markAsPaid(id);
                System.out.println("Transport marked as paid. Revenue updated.");
            }
        }
    }

    private void filterByDestination() {
        System.out.print("Enter destination to search: ");
        String dest = ctx.getInput().readLine();
        List<Transport> transports = ctx.getTransportService().findByDestination(ctx.getCurrentCompanyId(), dest);
        printTransportList(transports);
    }

    private void filterByDateRange() {
        System.out.print("Start date (yyyy-MM-dd): ");
        LocalDate startDate = ctx.getInput().readDate();
        System.out.print("End date (yyyy-MM-dd): ");
        LocalDate endDate = ctx.getInput().readDate();
        List<Transport> transports = ctx.getTransportService().findByDateRange(
                ctx.getCurrentCompanyId(), startDate, endDate);
        printTransportList(transports);
    }

    private void filterByTransportType() {
        System.out.println("1. Passengers");
        System.out.println("2. Goods");
        System.out.print("Choice: ");
        int choice = ctx.getInput().readInt();
        TransportType type = choice == 1 ? TransportType.PASSENGERS : TransportType.GOODS;
        List<Transport> transports = ctx.getTransportService().findByTransportType(ctx.getCurrentCompanyId(), type);
        printTransportList(transports);
    }
}
