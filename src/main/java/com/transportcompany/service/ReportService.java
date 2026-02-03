package com.transportcompany.service;

import com.transportcompany.entity.Employee;
import com.transportcompany.entity.Transport;
import com.transportcompany.entity.TransportCompany;
import com.transportcompany.repository.EmployeeRepository;
import com.transportcompany.repository.TransportCompanyRepository;
import com.transportcompany.repository.TransportRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class ReportService {

    private final TransportRepository transportRepository;
    private final EmployeeRepository employeeRepository;
    private final TransportCompanyRepository companyRepository;

    public ReportService() {
        this.transportRepository = new TransportRepository();
        this.employeeRepository = new EmployeeRepository();
        this.companyRepository = new TransportCompanyRepository();
    }

    // Report: Total transports count for a company
    public long getTotalTransportsCount(Long companyId) {
        return transportRepository.countByCompany(companyId);
    }

    // Report: Total revenue for a company
    public BigDecimal getTotalRevenue(Long companyId) {
        return transportRepository.getTotalRevenueByCompany(companyId);
    }

    // Report: Company revenue for a specific period
    public BigDecimal getRevenueForPeriod(Long companyId, LocalDate startDate, LocalDate endDate) {
        return transportRepository.getRevenueByDateRange(companyId, startDate, endDate);
    }

    // Report: Driver statistics (transports count and revenue)
    public List<DriverReport> getDriverStatistics(Long companyId) {
        List<Employee> employees = employeeRepository.findByCompanyId(companyId);
        List<DriverReport> reports = new ArrayList<>();

        for (Employee employee : employees) {
            DriverReport report = new DriverReport();
            report.driverId = employee.getId();
            report.driverName = employee.getFullName();
            report.transportCount = transportRepository.countByDriver(employee.getId());
            report.totalRevenue = transportRepository.getTotalRevenueByDriver(employee.getId());
            reports.add(report);
        }

        return reports.stream()
                .sorted((a, b) -> Long.compare(b.transportCount, a.transportCount))
                .collect(Collectors.toList());
    }

    // Report: Revenue by driver
    public Map<String, BigDecimal> getRevenueByDriver(Long companyId) {
        List<DriverReport> driverStats = getDriverStatistics(companyId);
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        for (DriverReport dr : driverStats) {
            result.put(dr.driverName, dr.totalRevenue);
        }
        return result;
    }

    // Report: Transports grouped by destination
    public Map<String, List<Transport>> getTransportsGroupedByDestination(Long companyId) {
        List<Transport> transports = transportRepository.findByCompanyId(companyId);
        return transports.stream()
                .collect(Collectors.groupingBy(Transport::getEndPoint));
    }

    // Report: Summary for company
    public CompanySummary getCompanySummary(Long companyId) {
        Optional<TransportCompany> companyOpt = companyRepository.findById(companyId);
        if (companyOpt.isEmpty()) {
            return null;
        }

        TransportCompany company = companyOpt.get();
        CompanySummary summary = new CompanySummary();
        summary.companyName = company.getName();
        summary.totalTransports = transportRepository.countByCompany(companyId);
        summary.totalRevenue = transportRepository.getTotalRevenueByCompany(companyId);
        summary.employeeCount = employeeRepository.findByCompanyId(companyId).size();

        List<Transport> transports = transportRepository.findByCompanyId(companyId);
        summary.paidTransports = transports.stream().filter(Transport::isPaid).count();
        summary.unpaidTransports = transports.stream().filter(t -> !t.isPaid()).count();
        summary.totalUnpaidAmount = transports.stream()
                .filter(t -> !t.isPaid())
                .map(Transport::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return summary;
    }

    // Generate text report
    public String generateTextReport(Long companyId) {
        CompanySummary summary = getCompanySummary(companyId);
        if (summary == null) {
            return "Company not found";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(60)).append("\n");
        sb.append("         COMPANY REPORT: ").append(summary.companyName).append("\n");
        sb.append("=".repeat(60)).append("\n\n");

        sb.append("SUMMARY\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append(String.format("Total Employees: %d%n", summary.employeeCount));
        sb.append(String.format("Total Transports: %d%n", summary.totalTransports));
        sb.append(String.format("Paid Transports: %d%n", summary.paidTransports));
        sb.append(String.format("Unpaid Transports: %d%n", summary.unpaidTransports));
        sb.append(String.format("Total Revenue (paid): %.2f BGN%n", summary.totalRevenue));
        sb.append(String.format("Pending Amount (unpaid): %.2f BGN%n", summary.totalUnpaidAmount));

        sb.append("\n\nDRIVER STATISTICS\n");
        sb.append("-".repeat(40)).append("\n");
        sb.append(String.format("%-25s %10s %15s%n", "Driver Name", "Transports", "Revenue"));
        sb.append("-".repeat(55)).append("\n");

        List<DriverReport> driverStats = getDriverStatistics(companyId);
        for (DriverReport dr : driverStats) {
            sb.append(String.format("%-25s %10d %15.2f%n", dr.driverName, dr.transportCount, dr.totalRevenue));
        }

        sb.append("\n").append("=".repeat(60)).append("\n");
        sb.append("Report generated: ").append(LocalDate.now()).append("\n");

        return sb.toString();
    }

    // Data classes for reports
    public static class DriverReport {
        public Long driverId;
        public String driverName;
        public long transportCount;
        public BigDecimal totalRevenue;

        @Override
        public String toString() {
            return String.format("Driver: %s, Transports: %d, Revenue: %.2f",
                    driverName, transportCount, totalRevenue);
        }
    }

    public static class CompanySummary {
        public String companyName;
        public long totalTransports;
        public BigDecimal totalRevenue;
        public int employeeCount;
        public long paidTransports;
        public long unpaidTransports;
        public BigDecimal totalUnpaidAmount;
    }
}
