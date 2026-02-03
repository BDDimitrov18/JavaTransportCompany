package com.transportcompany;

import com.transportcompany.cli.*;
import com.transportcompany.util.HibernateUtil;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("  Transport Company Management System");
        System.out.println("=========================================");

        Scanner scanner = new Scanner(System.in);
        InputHelper input = new InputHelper(scanner);
        MenuContext ctx = new MenuContext(input);

        // Initialize handlers
        CompanyMenuHandler companyHandler = new CompanyMenuHandler(ctx);
        EmployeeMenuHandler employeeHandler = new EmployeeMenuHandler(ctx);
        VehicleMenuHandler vehicleHandler = new VehicleMenuHandler(ctx);
        ClientMenuHandler clientHandler = new ClientMenuHandler(ctx);
        TransportMenuHandler transportHandler = new TransportMenuHandler(ctx);
        ReportMenuHandler reportHandler = new ReportMenuHandler(ctx);
        ExportImportHandler exportImportHandler = new ExportImportHandler(ctx);

        try {
            // Initialize database
            HibernateUtil.getSessionFactory();
            System.out.println("Database connected successfully.\n");

            boolean running = true;
            while (running) {
                if (ctx.getCurrentCompany() == null) {
                    running = companyHandler.showMainMenu();
                } else {
                    running = showCompanyMenu(ctx, companyHandler, employeeHandler, vehicleHandler,
                            clientHandler, transportHandler, reportHandler, exportImportHandler);
                }
            }
        } finally {
            HibernateUtil.shutdown();
            scanner.close();
            System.out.println("Goodbye!");
        }
    }

    private static boolean showCompanyMenu(MenuContext ctx,
                                           CompanyMenuHandler companyHandler,
                                           EmployeeMenuHandler employeeHandler,
                                           VehicleMenuHandler vehicleHandler,
                                           ClientMenuHandler clientHandler,
                                           TransportMenuHandler transportHandler,
                                           ReportMenuHandler reportHandler,
                                           ExportImportHandler exportImportHandler) {
        System.out.println("\n=== " + ctx.getCurrentCompany().getName().toUpperCase() + " ===");
        System.out.println("1. Employees");
        System.out.println("2. Vehicles");
        System.out.println("3. Clients");
        System.out.println("4. Transports");
        System.out.println("5. Reports");
        System.out.println("6. Export/Import");
        System.out.println("7. Edit company");
        System.out.println("8. Back to main menu");
        System.out.println("0. Exit");
        System.out.print("Choice: ");

        int choice = ctx.getInput().readInt();
        switch (choice) {
            case 1 -> employeeHandler.showMenu();
            case 2 -> vehicleHandler.showMenu();
            case 3 -> clientHandler.showMenu();
            case 4 -> transportHandler.showMenu();
            case 5 -> reportHandler.showMenu();
            case 6 -> exportImportHandler.showMenu();
            case 7 -> companyHandler.editCompany();
            case 8 -> ctx.setCurrentCompany(null);
            case 0 -> { return false; }
            default -> System.out.println("Invalid choice.");
        }
        return true;
    }
}
