package com.transportcompany.cli;

import com.transportcompany.entity.TransportCompany;
import com.transportcompany.service.*;

public class MenuContext {

    private final TransportCompanyService companyService;
    private final EmployeeService employeeService;
    private final ClientService clientService;
    private final VehicleService vehicleService;
    private final TransportService transportService;
    private final ReportService reportService;
    private final InputHelper input;

    private TransportCompany currentCompany;

    public MenuContext(InputHelper input) {
        this.input = input;
        this.companyService = new TransportCompanyService();
        this.employeeService = new EmployeeService();
        this.clientService = new ClientService();
        this.vehicleService = new VehicleService();
        this.transportService = new TransportService();
        this.reportService = new ReportService();
    }

    public TransportCompanyService getCompanyService() { return companyService; }
    public EmployeeService getEmployeeService() { return employeeService; }
    public ClientService getClientService() { return clientService; }
    public VehicleService getVehicleService() { return vehicleService; }
    public TransportService getTransportService() { return transportService; }
    public ReportService getReportService() { return reportService; }
    public InputHelper getInput() { return input; }

    public TransportCompany getCurrentCompany() { return currentCompany; }
    public void setCurrentCompany(TransportCompany company) { this.currentCompany = company; }
    public Long getCurrentCompanyId() { return currentCompany != null ? currentCompany.getId() : null; }
}
