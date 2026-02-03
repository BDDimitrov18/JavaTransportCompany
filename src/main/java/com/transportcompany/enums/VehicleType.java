package com.transportcompany.enums;

public enum VehicleType {
    BUS("Автобус"),
    TRUCK("Камион"),
    TANKER("Цистерна"),
    VAN("Бус"),
    CAR("Лек автомобил");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
