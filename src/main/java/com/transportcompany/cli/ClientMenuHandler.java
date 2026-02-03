package com.transportcompany.cli;

import com.transportcompany.entity.Client;
import com.transportcompany.validation.ValidationException;

import java.util.List;
import java.util.Optional;

public class ClientMenuHandler {

    private final MenuContext ctx;

    public ClientMenuHandler(MenuContext ctx) {
        this.ctx = ctx;
    }

    public void showMenu() {
        while (true) {
            System.out.println("\n--- Clients Menu ---");
            System.out.println("1. Add client");
            System.out.println("2. List clients");
            System.out.println("3. Edit client");
            System.out.println("4. Delete client");
            System.out.println("5. Show clients with unpaid transports");
            System.out.println("0. Back");
            System.out.print("Choice: ");

            int choice = ctx.getInput().readInt();
            switch (choice) {
                case 1 -> addClient();
                case 2 -> listClients();
                case 3 -> editClient();
                case 4 -> deleteClient();
                case 5 -> showClientsWithUnpaid();
                case 0 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addClient() {
        System.out.println("\n--- Add Client ---");
        System.out.print("Client name: ");
        String name = ctx.getInput().readLine();
        System.out.print("Contact person: ");
        String contact = ctx.getInput().readLine();
        System.out.print("Phone: ");
        String phone = ctx.getInput().readLine();
        System.out.print("Email: ");
        String email = ctx.getInput().readLine();

        try {
            Client client = ctx.getClientService().create(
                    ctx.getCurrentCompanyId(), name, contact, phone, email);
            System.out.println("Client added! ID: " + client.getId());
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listClients() {
        List<Client> clients = ctx.getClientService().findByCompanyId(ctx.getCurrentCompanyId());
        printClientList(clients);
    }

    public void printClientList(List<Client> clients) {
        if (clients.isEmpty()) {
            System.out.println("No clients found.");
            return;
        }

        System.out.printf("%-5s %-25s %-20s %-15s %-25s%n", "ID", "Name", "Contact", "Phone", "Email");
        System.out.println("-".repeat(95));
        for (Client c : clients) {
            System.out.printf("%-5d %-25s %-20s %-15s %-25s%n",
                    c.getId(), c.getName(),
                    c.getContactPerson() != null ? c.getContactPerson() : "-",
                    c.getPhone() != null ? c.getPhone() : "-",
                    c.getEmail() != null ? c.getEmail() : "-");
        }
    }

    private void editClient() {
        listClients();
        System.out.print("Enter client ID to edit: ");
        Long id = ctx.getInput().readLong();
        Optional<Client> clientOpt = ctx.getClientService().findById(id);
        if (clientOpt.isEmpty()) {
            System.out.println("Client not found.");
            return;
        }

        Client c = clientOpt.get();
        System.out.print("New name (Enter to keep '" + c.getName() + "'): ");
        String name = ctx.getInput().readLine();
        if (!name.isBlank()) c.setName(name);

        System.out.print("New contact person (Enter to keep): ");
        String contact = ctx.getInput().readLine();
        if (!contact.isBlank()) c.setContactPerson(contact);

        System.out.print("New phone (Enter to keep): ");
        String phone = ctx.getInput().readLine();
        if (!phone.isBlank()) c.setPhone(phone);

        System.out.print("New email (Enter to keep): ");
        String email = ctx.getInput().readLine();
        if (!email.isBlank()) c.setEmail(email);

        try {
            ctx.getClientService().update(c);
            System.out.println("Client updated.");
        } catch (ValidationException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void deleteClient() {
        listClients();
        System.out.print("Enter client ID to delete: ");
        Long id = ctx.getInput().readLong();
        ctx.getClientService().delete(id);
        System.out.println("Client deleted.");
    }

    private void showClientsWithUnpaid() {
        List<Client> clients = ctx.getClientService().findClientsWithUnpaidTransports(ctx.getCurrentCompanyId());
        if (clients.isEmpty()) {
            System.out.println("All clients have paid their transports!");
        } else {
            System.out.println("\nClients with unpaid transports:");
            printClientList(clients);
        }
    }
}
