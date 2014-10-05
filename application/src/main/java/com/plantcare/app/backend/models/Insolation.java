package com.plantcare.app.backend.models;

public enum Insolation {

    FULL("full"),
    INDIRECT("indirect"),
    PARTIALLY_SHADED("partialy_shaded");

    private String insolation;

    Insolation(String insolation) {
        this.insolation = insolation;
    }

    public String getInsolation() {
        return this.insolation;
    }

    public static Insolation fromString(String insolation) {
        if (insolation != null) {
            for (Insolation insolationEnum : Insolation.values()) {
                if (insolation.equalsIgnoreCase(insolationEnum.insolation)) {
                    return insolationEnum;
                }
            }
        }
        return null;
    }
}
