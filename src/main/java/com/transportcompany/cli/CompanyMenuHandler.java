package com.transportcompany.cli;

import com.transportcompany.entity.TransportCompany;
import com.transportcompany.validation.ValidationException;

import java.util.List;
import java.util.Optional;

public class CompanyMenuHandler {

    private final MenuContext ctx;

    public CompanyMenuHandler(MenuContext ctx) {
        this.ctx = ctx;
    }

    public boolean showMainMenu() {
        System.out.println("\n=== MAIN MENU ===");
        System.out.println("1. Create new company");
        System.out.println("2. Select company");
        System.out.println("3. List all companies");
        System.out.println("4. Delete company");
        System.out.println("0. Exit");
        System.out.print("Choice: ");

        int choice = ctx.getInput().readInt();
        switch (choice) {
            case 1 -> createCompany();
            case 2 -> selectCompany();
            case 3 -> listCompanies();
            case 4 -> deleteCompany();
            case 0 -> { return false; }
            default -> System.out.println("Invalid choice.");
        }
        return true;
    }

    public void createCompany() {
        System.out.println("\n--- Create Company ---");
        System.out.print("Company name: ");
        String name = ctx.getInput().readLine();
        System.out.print("Address: ");
        String address = ctx.getInput().readLine();

        try {
            TransportCompany company = ctx.getCompanyService().create(name, address);
            System.out.println("Company created successfully! ID: " + company.getId());
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void selectCompany() {
        listCompanies();
        System.out.print("Enter company ID: ");
        Long id = ctx.getInput().readLong();
        Optional<TransportCompany> company = ctx.getCompanyService().findById(id);
        if (company.isPresent()) {
            ctx.setCurrentCompany(company.get());
            System.out.println("Selected: " + ctx.getCurrentCompany().getName());
        } else {
            System.out.println("Company not found.");
        }
    }

    public void listCompanies() {
        System.out.println("\n--- Companies ---");
        System.out.println("Sort by: 1. Name | 2. Revenue");
        int sortChoice = ctx.getInput().readInt();

        List<TransportCompany> companies = sortChoice == 2
                ? ctx.getCompanyService().findAllSortedByRevenue()
                : ctx.getCompanyService().findAllSortedByName();

        if (companies.isEmpty()) {
            System.out.println("No companies found.");
            return;
        }

        System.out.printf("%-5s %-30s %-40s %15s%n", "ID", "Name", "Address", "Revenue");
        System.out.println("-".repeat(95));
        for (TransportCompany c : companies) {
            System.out.printf("%-5d %-30s %-40s %15.2f%n",
                    c.getId(), c.getName(), c.getAddress() != null ? c.getAddress() : "", c.getRevenue());
        }
    }

    public void deleteCompany() {
        listCompanies();
        System.out.print("Enter company ID to delete: ");
        Long id = ctx.getInput().readLong();
        if (ctx.getInput().confirm("Are you sure?")) {
            ctx.getCompanyService().delete(id);
            System.out.println("Company deleted.");
        }
    }

    public void editCompany() {
        System.out.println("\n--- Edit Company ---");
        TransportCompany current = ctx.getCurrentCompany();

        System.out.println("Current name: " + current.getName());
        System.out.print("New name (press Enter to keep): ");
        String name = ctx.getInput().readLine();
        if (!name.isBlank()) {
            current.setName(name);
        }

        System.out.println("Current address: " + current.getAddress());
        System.out.print("New address (press Enter to keep): ");
        String address = ctx.getInput().readLine();
        if (!address.isBlank()) {
            current.setAddress(address);
        }

        try {
            ctx.setCurrentCompany(ctx.getCompanyService().update(current));
            System.out.println("Company updated successfully.");
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
