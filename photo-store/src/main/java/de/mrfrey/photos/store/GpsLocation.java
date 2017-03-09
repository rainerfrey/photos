package de.mrfrey.photos.store;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GpsLocation {
    private final double latitude;
    private final double longitude;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public GpsLocation(@JsonProperty("latitude") double latitude, @JsonProperty("longitude") double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
