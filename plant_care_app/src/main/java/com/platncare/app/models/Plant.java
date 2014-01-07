package com.platncare.app.models;

import java.io.Serializable;

public class Plant implements Serializable{

    private String plantName;
    private String plantImageUrl;

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public String getPlantImageUrl() {
        return plantImageUrl;
    }

    public void setPlantImageUrl(String plantImageUrl) {
        this.plantImageUrl = plantImageUrl;
    }


}
