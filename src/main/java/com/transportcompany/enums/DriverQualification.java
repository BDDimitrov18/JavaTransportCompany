package com.transportcompany.enums;

public enum DriverQualification {
    STANDARD("Стандартна правоспособност"),
    HAZARDOUS_MATERIALS("Превоз на опасни товари"),
    FLAMMABLE_MATERIALS("Превоз на леснозапалими товари"),
    PASSENGER_TRANSPORT("Превоз на над 12 пътника"),
    HEAVY_CARGO("Превоз на тежки товари"),
    SPECIAL_CARGO("Превоз на специални товари");

    private final String displayName;

    DriverQualification(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
