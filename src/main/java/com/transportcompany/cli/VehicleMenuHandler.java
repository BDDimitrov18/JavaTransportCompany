package com.transportcompany.cli;

import com.transportcompany.entity.Vehicle;
import com.transportcompany.enums.VehicleType;
import com.transportcompany.validation.ValidationException;

import java.util.List;
import java.util.Optional;

public class VehicleMenuHandler {

    private final MenuContext ctx;

    public VehicleMenuHandler(MenuContext ctx) {
        this.ctx = ctx;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Vehicles Menu ---");
            System.out.println("1. Add vehicle");
            System.out.println("2. List vehicles");
            System.out.println("3. Edit vehicle");
            System.out.println("4. Delete vehicle");
            System.out.println("5. Filter by type");
            System.out.println("0. Back");
            System.out.print("Choice: ");

            int choice = ctx.getInput().readInt();
            switch (choice) {
                case 1 -> addVehicle();
                case 2 -> listVehicles();
                case 3 -> editVehicle();
                case 4 -> deleteVehicle();
                case 5 -> filterByType();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addVehicle() {
        System.out.println("\n--- Add Vehicle ---");
        System.out.print("Registration number: ");
        String regNum = ctx.getInput().readLine();

        System.out.println("Vehicle type:");
        VehicleType[] types = VehicleType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i].getDisplayName());
        }
        System.out.print("Choice: ");
        int typeChoice = ctx.getInput().readInt();
        VehicleType vehicleType = typeChoice > 0 && typeChoice <= types.length ? types[typeChoice - 1] : VehicleType.TRUCK;

        System.out.print("Brand: ");
        String brand = ctx.getInput().readLine();
        System.out.print("Model: ");
        String model = ctx.getInput().readLine();
        System.out.print("Year: ");
        Integer year = ctx.getInput().readIntOrNull();
        System.out.print("Capacity (seats or tons): ");
        Integer capacity = ctx.getInput().readIntOrNull();

        try {
            Vehicle vehicle = ctx.getVehicleService().create(
                    ctx.getCurrentCompanyId(), regNum, vehicleType, brand, model, year, capacity);
            System.out.println("Vehicle added! ID: " + vehicle.getId());
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listVehicles() {
        List<Vehicle> vehicles = ctx.getVehicleService().findByCompanyId(ctx.getCurrentCompanyId());
        printVehicleList(vehicles);
    }

    public void printVehicleList(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.printf("%-5s %-15s %-15s %-15s %-10s %6s %10s%n",
                "ID", "Reg. Number", "Type", "Brand", "Model", "Year", "Capacity");
        System.out.println("-".repeat(85));
        for (Vehicle v : vehicles) {
            System.out.printf("%-5d %-15s %-15s %-15s %-10s %6s %10s%n",
                    v.getId(), v.getRegistrationNumber(), v.getVehicleType().getDisplayName(),
                    v.getBrand() != null ? v.getBrand() : "-",
                    v.getModel() != null ? v.getModel() : "-",
                    v.getYear() != null ? v.getYear() : "-",
                    v.getCapacity() != null ? v.getCapacity() : "-");
        }
    }

    private void editVehicle() {
        listVehicles();
        System.out.print("Enter vehicle ID to edit: ");
        Long id = ctx.getInput().readLong();
        Optional<Vehicle> vehOpt = ctx.getVehicleService().findById(id);
        if (vehOpt.isEmpty()) {
            System.out.println("Vehicle not found.");
            return;
        }

        Vehicle v = vehOpt.get();
        System.out.print("New brand (Enter to keep '" + v.getBrand() + "'): ");
        String brand = ctx.getInput().readLine();
        if (!brand.isBlank()) v.setBrand(brand);

        System.out.print("New model (Enter to keep '" + v.getModel() + "'): ");
        String model = ctx.getInput().readLine();
        if (!model.isBlank()) v.setModel(model);

        System.out.print("New capacity (Enter to keep): ");
        String capStr = ctx.getInput().readLine();
        if (!capStr.isBlank()) v.setCapacity(Integer.parseInt(capStr));

        try {
            ctx.getVehicleService().update(v);
            System.out.println("Vehicle updated.");
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteVehicle() {
        listVehicles();
        System.out.print("Enter vehicle ID to delete: ");
        Long id = ctx.getInput().readLong();
        ctx.getVehicleService().delete(id);
        System.out.println("Vehicle deleted.");
    }

    private void filterByType() {
        System.out.println("Select type:");
        VehicleType[] types = VehicleType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.println((i + 1) + ". " + types[i].getDisplayName());
        }
        int choice = ctx.getInput().readInt();
        if (choice > 0 && choice <= types.length) {
            List<Vehicle> vehicles = ctx.getVehicleService().findByVehicleType(
                    ctx.getCurrentCompanyId(), types[choice - 1]);
            printVehicleList(vehicles);
        }
    }
}
