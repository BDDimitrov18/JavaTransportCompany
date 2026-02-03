package com.transportcompany.cli;

import com.transportcompany.entity.Employee;
import com.transportcompany.enums.DriverQualification;
import com.transportcompany.validation.ValidationException;

import java.math.BigDecimal;
import java.util.*;

public class EmployeeMenuHandler {

    private final MenuContext ctx;

    public EmployeeMenuHandler(MenuContext ctx) {
        this.ctx = ctx;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Employees Menu ---");
            System.out.println("1. Add employee");
            System.out.println("2. List employees");
            System.out.println("3. Edit employee");
            System.out.println("4. Delete employee");
            System.out.println("5. Filter by qualification");
            System.out.println("6. Filter by salary range");
            System.out.println("0. Back");
            System.out.print("Choice: ");

            int choice = ctx.getInput().readInt();
            switch (choice) {
                case 1 -> addEmployee();
                case 2 -> listEmployees();
                case 3 -> editEmployee();
                case 4 -> deleteEmployee();
                case 5 -> filterByQualification();
                case 6 -> filterBySalary();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addEmployee() {
        System.out.println("\n--- Add Employee ---");
        System.out.print("First name: ");
        String firstName = ctx.getInput().readLine();
        System.out.print("Last name: ");
        String lastName = ctx.getInput().readLine();
        System.out.print("Salary: ");
        BigDecimal salary = ctx.getInput().readBigDecimal();
        System.out.print("Phone (optional): ");
        String phone = ctx.getInput().readLine();

        Set<DriverQualification> qualifications = selectQualifications();

        try {
            Employee employee = ctx.getEmployeeService().create(
                    ctx.getCurrentCompanyId(), firstName, lastName, salary, qualifications);
            if (!phone.isBlank()) {
                employee.setPhone(phone);
                ctx.getEmployeeService().update(employee);
            }
            System.out.println("Employee added successfully! ID: " + employee.getId());
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Set<DriverQualification> selectQualifications() {
        System.out.println("Available qualifications:");
        DriverQualification[] quals = DriverQualification.values();
        for (int i = 0; i < quals.length; i++) {
            System.out.println((i + 1) + ". " + quals[i].getDisplayName());
        }
        System.out.print("Enter numbers separated by comma (e.g., 1,3): ");
        String input = ctx.getInput().readLine();

        Set<DriverQualification> selected = new HashSet<>();
        if (!input.isBlank()) {
            for (String s : input.split(",")) {
                try {
                    int idx = Integer.parseInt(s.trim()) - 1;
                    if (idx >= 0 && idx < quals.length) {
                        selected.add(quals[idx]);
                    }
                } catch (NumberFormatException ignored) {}
            }
        }
        return selected;
    }

    private void listEmployees() {
        System.out.println("\nSort by: 1. Name | 2. Salary (asc) | 3. Salary (desc)");
        int sortChoice = ctx.getInput().readInt();

        List<Employee> employees;
        if (sortChoice == 2) {
            employees = ctx.getEmployeeService().findByCompanyIdSortedBySalary(ctx.getCurrentCompanyId(), true);
        } else if (sortChoice == 3) {
            employees = ctx.getEmployeeService().findByCompanyIdSortedBySalary(ctx.getCurrentCompanyId(), false);
        } else {
            employees = ctx.getEmployeeService().findByCompanyId(ctx.getCurrentCompanyId());
        }

        printEmployeeList(employees);
    }

    public void printEmployeeList(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }

        System.out.printf("%-5s %-25s %-15s %12s %-30s%n", "ID", "Name", "Phone", "Salary", "Qualifications");
        System.out.println("-".repeat(100));
        for (Employee e : employees) {
            String quals = e.getQualifications().stream()
                    .map(q -> q.name().substring(0, Math.min(4, q.name().length())))
                    .reduce((a, b) -> a + "," + b)
                    .orElse("-");
            System.out.printf("%-5d %-25s %-15s %12.2f %-30s%n",
                    e.getId(), e.getFullName(), e.getPhone() != null ? e.getPhone() : "-",
                    e.getSalary(), quals);
        }
    }

    private void editEmployee() {
        listEmployees();
        System.out.print("Enter employee ID to edit: ");
        Long id = ctx.getInput().readLong();
        Optional<Employee> empOpt = ctx.getEmployeeService().findById(id);
        if (empOpt.isEmpty()) {
            System.out.println("Employee not found.");
            return;
        }

        Employee emp = empOpt.get();
        System.out.print("New first name (Enter to keep '" + emp.getFirstName() + "'): ");
        String fn = ctx.getInput().readLine();
        if (!fn.isBlank()) emp.setFirstName(fn);

        System.out.print("New last name (Enter to keep '" + emp.getLastName() + "'): ");
        String ln = ctx.getInput().readLine();
        if (!ln.isBlank()) emp.setLastName(ln);

        System.out.print("New salary (Enter to keep " + emp.getSalary() + "): ");
        String salStr = ctx.getInput().readLine();
        if (!salStr.isBlank()) emp.setSalary(new BigDecimal(salStr));

        System.out.print("Update qualifications? (yes/no): ");
        if (ctx.getInput().readLine().equalsIgnoreCase("yes")) {
            emp.setQualifications(selectQualifications());
        }

        try {
            ctx.getEmployeeService().update(emp);
            System.out.println("Employee updated.");
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteEmployee() {
        listEmployees();
        System.out.print("Enter employee ID to delete: ");
        Long id = ctx.getInput().readLong();
        ctx.getEmployeeService().delete(id);
        System.out.println("Employee deleted.");
    }

    private void filterByQualification() {
        System.out.println("Select qualification:");
        DriverQualification[] quals = DriverQualification.values();
        for (int i = 0; i < quals.length; i++) {
            System.out.println((i + 1) + ". " + quals[i].getDisplayName());
        }
        int choice = ctx.getInput().readInt();
        if (choice > 0 && choice <= quals.length) {
            List<Employee> employees = ctx.getEmployeeService().findByQualification(
                    ctx.getCurrentCompanyId(), quals[choice - 1]);
            printEmployeeList(employees);
        }
    }

    private void filterBySalary() {
        System.out.print("Minimum salary: ");
        BigDecimal min = ctx.getInput().readBigDecimal();
        System.out.print("Maximum salary: ");
        BigDecimal max = ctx.getInput().readBigDecimal();
        List<Employee> employees = ctx.getEmployeeService().findBySalaryRange(ctx.getCurrentCompanyId(), min, max);
        printEmployeeList(employees);
    }
}
