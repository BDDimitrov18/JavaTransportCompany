package com.transportcompany.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.transportcompany.entity.Transport;
import com.transportcompany.enums.TransportType;
import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static final Gson gson = createGson();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private static Gson createGson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    public static void exportTransportsToJson(List<Transport> transports, String filePath) throws IOException {
        List<TransportDTO> dtos = transports.stream()
                .map(TransportDTO::fromTransport)
                .toList();

        String json = gson.toJson(dtos);
        Files.writeString(Paths.get(filePath), json);
    }

    public static List<TransportDTO> importTransportsFromJson(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        String json = Files.readString(path);
        TransportDTO[] dtos = gson.fromJson(json, TransportDTO[].class);
        return dtos != null ? List.of(dtos) : new ArrayList<>();
    }

    public static void exportTransportsToCsv(List<Transport> transports, String filePath) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Header
            writer.println("ID,Start Point,End Point,Departure Date,Arrival Date,Type,Cargo Description," +
                    "Cargo Weight,Passenger Count,Price,Paid,Driver,Vehicle,Client");

            // Data
            for (Transport t : transports) {
                writer.printf("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
                        t.getId(),
                        escapeCsv(t.getStartPoint()),
                        escapeCsv(t.getEndPoint()),
                        t.getDepartureDate(),
                        t.getArrivalDate(),
                        t.getTransportType(),
                        escapeCsv(t.getCargoDescription() != null ? t.getCargoDescription() : ""),
                        t.getCargoWeight() != null ? t.getCargoWeight() : "",
                        t.getPassengerCount() != null ? t.getPassengerCount() : "",
                        t.getPrice(),
                        t.isPaid(),
                        t.getDriver() != null ? escapeCsv(t.getDriver().getFullName()) : "",
                        t.getVehicle() != null ? escapeCsv(t.getVehicle().getRegistrationNumber()) : "",
                        t.getClient() != null ? escapeCsv(t.getClient().getName()) : ""
                );
            }
        }
    }

    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    // DTO for JSON serialization
    public static class TransportDTO {
        public Long id;
        public String startPoint;
        public String endPoint;
        public String departureDate;
        public String arrivalDate;
        public String transportType;
        public String cargoDescription;
        public Double cargoWeight;
        public Integer passengerCount;
        public BigDecimal price;
        public boolean paid;
        public String driverName;
        public String vehicleRegNumber;
        public String clientName;

        public static TransportDTO fromTransport(Transport t) {
            TransportDTO dto = new TransportDTO();
            dto.id = t.getId();
            dto.startPoint = t.getStartPoint();
            dto.endPoint = t.getEndPoint();
            dto.departureDate = t.getDepartureDate().format(DATE_FORMATTER);
            dto.arrivalDate = t.getArrivalDate().format(DATE_FORMATTER);
            dto.transportType = t.getTransportType().name();
            dto.cargoDescription = t.getCargoDescription();
            dto.cargoWeight = t.getCargoWeight();
            dto.passengerCount = t.getPassengerCount();
            dto.price = t.getPrice();
            dto.paid = t.isPaid();
            dto.driverName = t.getDriver() != null ? t.getDriver().getFullName() : null;
            dto.vehicleRegNumber = t.getVehicle() != null ? t.getVehicle().getRegistrationNumber() : null;
            dto.clientName = t.getClient() != null ? t.getClient().getName() : null;
            return dto;
        }

        public LocalDate getParsedDepartureDate() {
            return LocalDate.parse(departureDate, DATE_FORMATTER);
        }

        public LocalDate getParsedArrivalDate() {
            return LocalDate.parse(arrivalDate, DATE_FORMATTER);
        }

        public TransportType getParsedTransportType() {
            return TransportType.valueOf(transportType);
        }
    }

    // Type adapter for LocalDate
    private static class LocalDateAdapter extends TypeAdapter<LocalDate> {
        @Override
        public void write(JsonWriter out, LocalDate value) throws IOException {
            out.value(value != null ? value.format(DATE_FORMATTER) : null);
        }

        @Override
        public LocalDate read(JsonReader in) throws IOException {
            String value = in.nextString();
            return value != null ? LocalDate.parse(value, DATE_FORMATTER) : null;
        }
    }
}
