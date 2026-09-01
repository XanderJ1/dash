package com.bash.dash.location.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

@Entity
@NoArgsConstructor
@Data
public class DriverLocation implements Serializable {

    @Id
    private Long driverProfileId;

    @Embedded
    GeoPoint location;
    private Instant updatedAt;

    public DriverLocation(Long driverProfileId, GeoPoint location) {
        this.driverProfileId = driverProfileId;
        this.location = location;
    }

}
