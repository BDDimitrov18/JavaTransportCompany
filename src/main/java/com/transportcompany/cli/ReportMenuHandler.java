package com.transportcompany.cli;

import com.transportcompany.service.ReportService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReportMenuHandler {

    private final MenuContext ctx;

    public ReportMenuHandler(MenuContext ctx) {
        this.ctx = ctx;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Reports Menu ---");
            System.out.println("1. Company summary");
            System.out.println("2. Driver statistics");
            System.out.println("3. Revenue for period");
            System.out.println("4. Generate full report");
            System.out.println("0. Back");
            System.out.print("Choice: ");

            int choice = ctx.getInput().readInt();
            switch (choice) {
                case 1 -> showCompanySummary();
                case 2 -> showDriverStatistics();
                case 3 -> showRevenueForPeriod();
                case 4 -> generateFullReport();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void showCompanySummary() {
        ReportService.CompanySummary summary = ctx.getReportService().getCompanySummary(ctx.getCurrentCompanyId());
        if (summary == null) {
            System.out.println("Could not generate summary.");
            return;
        }

        System.out.println("\n=== Company Summary ===");
        System.out.println("Company: " + summary.companyName);
        System.out.println("Employees: " + summary.employeeCount);
        System.out.println("Total transports: " + summary.totalTransports);
        System.out.println("Paid transports: " + summary.paidTransports);
        System.out.println("Unpaid transports: " + summary.unpaidTransports);
        System.out.printf("Total revenue: %.2f BGN%n", summary.totalRevenue);
        System.out.printf("Pending amount: %.2f BGN%n", summary.totalUnpaidAmount);
    }

    private void showDriverStatistics() {
        List<ReportService.DriverReport> stats = ctx.getReportService().getDriverStatistics(ctx.getCurrentCompanyId());
        if (stats.isEmpty()) {
            System.out.println("No driver statistics available.");
            return;
        }

        System.out.println("\n=== Driver Statistics ===");
        System.out.printf("%-25s %12s %15s%n", "Driver", "Transports", "Revenue");
        System.out.println("-".repeat(55));
        for (ReportService.DriverReport dr : stats) {
            System.out.printf("%-25s %12d %15.2f%n", dr.driverName, dr.transportCount, dr.totalRevenue);
        }
    }

    private void showRevenueForPeriod() {
        System.out.print("Start date (yyyy-MM-dd): ");
        LocalDate startDate = ctx.getInput().readDate();
        System.out.print("End date (yyyy-MM-dd): ");
        LocalDate endDate = ctx.getInput().readDate();

        BigDecimal revenue = ctx.getReportService().getRevenueForPeriod(ctx.getCurrentCompanyId(), startDate, endDate);
        System.out.printf("%nRevenue from %s to %s: %.2f BGN%n", startDate, endDate, revenue);
    }

    private void generateFullReport() {
        String report = ctx.getReportService().generateTextReport(ctx.getCurrentCompanyId());
        System.out.println(report);

        if (ctx.getInput().confirm("Save to file?")) {
            System.out.print("File path: ");
            String path = ctx.getInput().readLine();
            try {
                java.nio.file.Files.writeString(java.nio.file.Paths.get(path), report);
                System.out.println("Report saved to " + path);
            } catch (IOException e) {
                System.out.println("Error saving report: " + e.getMessage());
            }
        }
    }
}
