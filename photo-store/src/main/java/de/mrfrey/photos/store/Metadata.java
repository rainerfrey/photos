package de.mrfrey.photos.store;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.LinkedHashMap;

public class Metadata {
    @JsonProperty("Model")
    private String model;
    @JsonProperty("Date/Time")
    private Date date;
    @JsonProperty("Lens Model")
    private String lens;
    @JsonProperty("Aperture Value")
    private String aperture;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLens() {
        return lens;
    }

    public void setLens(String lens) {
        this.lens = lens;
    }

    public String getAperture() {
        return aperture;
    }

    public void setAperture(String aperture) {
        this.aperture = aperture;
    }
}
