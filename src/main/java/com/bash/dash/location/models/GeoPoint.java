package com.bash.dash.location.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class GeoPoint {
    private Double lat;
    private Double lng;

    public GeoPoint(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public GeoPoint() {

    }
}