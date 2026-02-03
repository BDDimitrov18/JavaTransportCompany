package com.transportcompany.cli;

import com.transportcompany.entity.Transport;
import com.transportcompany.util.FileUtil;

import java.io.IOException;
import java.util.List;

public class ExportImportHandler {

    private final MenuContext ctx;

    public ExportImportHandler(MenuContext ctx) {
        this.ctx = ctx;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Export/Import Menu ---");
            System.out.println("1. Export transports to JSON");
            System.out.println("2. Export transports to CSV");
            System.out.println("3. Import transports from JSON");
            System.out.println("0. Back");
            System.out.print("Choice: ");

            int choice = ctx.getInput().readInt();
            switch (choice) {
                case 1 -> exportToJson();
                case 2 -> exportToCsv();
                case 3 -> importFromJson();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void exportToJson() {
        List<Transport> transports = ctx.getTransportService().findByCompanyId(ctx.getCurrentCompanyId());
        System.out.print("Enter file path (e.g., transports.json): ");
        String path = ctx.getInput().readLine();

        try {
            FileUtil.exportTransportsToJson(transports, path);
            System.out.println("Exported " + transports.size() + " transports to " + path);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void exportToCsv() {
        List<Transport> transports = ctx.getTransportService().findByCompanyId(ctx.getCurrentCompanyId());
        System.out.print("Enter file path (e.g., transports.csv): ");
        String path = ctx.getInput().readLine();

        try {
            FileUtil.exportTransportsToCsv(transports, path);
            System.out.println("Exported " + transports.size() + " transports to " + path);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void importFromJson() {
        System.out.print("Enter JSON file path: ");
        String path = ctx.getInput().readLine();

        try {
            List<FileUtil.TransportDTO> dtos = FileUtil.importTransportsFromJson(path);
            System.out.println("Found " + dtos.size() + " transports in file.");
            System.out.println("Note: Imported data is displayed but not automatically added to database.");
            System.out.println("To add transports, use the 'Add transport' function.");

            for (FileUtil.TransportDTO dto : dtos) {
                System.out.printf("  %s -> %s | %s | %.2f%n",
                        dto.startPoint, dto.endPoint, dto.transportType, dto.price);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
