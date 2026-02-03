package com.transportcompany.cli;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class InputHelper {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine() {
        return scanner.nextLine();
    }

    public int readInt() {
        try {
            String line = scanner.nextLine();
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public Integer readIntOrNull() {
        try {
            String line = scanner.nextLine();
            if (line.isBlank()) return null;
            return Integer.parseInt(line.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Long readLong() {
        try {
            String line = scanner.nextLine();
            return Long.parseLong(line.trim());
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public Double readDouble() {
        try {
            String line = scanner.nextLine();
            return Double.parseDouble(line.trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public BigDecimal readBigDecimal() {
        try {
            String line = scanner.nextLine();
            return new BigDecimal(line.trim());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    public LocalDate readDate() {
        while (true) {
            try {
                String line = scanner.nextLine();
                return LocalDate.parse(line.trim(), DATE_FORMAT);
            } catch (DateTimeParseException e) {
                System.out.print("Invalid date format. Please use yyyy-MM-dd: ");
            }
        }
    }

    public boolean confirm(String message) {
        System.out.print(message + " (yes/no): ");
        return scanner.nextLine().equalsIgnoreCase("yes");
    }
}
