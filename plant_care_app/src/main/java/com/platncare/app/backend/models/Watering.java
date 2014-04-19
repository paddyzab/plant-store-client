package com.platncare.app.backend.models;

public enum Watering {

    FUGAL("fugal"),
    MODERATE("moderate"),
    PLENTIFUL("plentiful");

    private String watering;

    Watering(String watering) {
        this.watering = watering;
    }

    public String getWatering() {
        return this.watering;
    }

    public static Watering fromString(String watering) {
        if (watering != null) {
            for (Watering wateringEnum : Watering.values()) {
                if (watering.equalsIgnoreCase(wateringEnum.watering)) {
                    return wateringEnum;
                }
            }
        }
        return null;
    }
}
