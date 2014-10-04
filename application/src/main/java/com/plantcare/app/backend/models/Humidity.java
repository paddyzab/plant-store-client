package com.plantcare.app.backend.models;

public enum Humidity {

    LOW("low"),
    MODERATE("moderate"),
    HGH("high");

    private String humidity;

    Humidity(String humidity) {
        this.humidity = humidity;
    }

    public String getHumidity() {
        return this.humidity;
    }

    public static Humidity fromString(String humidity) {
        if (humidity != null) {
            for (Humidity humidityEnum : Humidity.values()) {
                if (humidity.equalsIgnoreCase(humidityEnum.humidity)) {
                    return humidityEnum;
                }
            }
        }
        return null;
    }
}

