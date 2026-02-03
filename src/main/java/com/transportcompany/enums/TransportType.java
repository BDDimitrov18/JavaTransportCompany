package com.transportcompany.enums;

public enum TransportType {
    PASSENGERS("Пътници"),
    GOODS("Товари");

    private final String displayName;

    TransportType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
