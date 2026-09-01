package com.bash.dash.rides.models;

import com.bash.dash.users.domain.DriverProfile;
import com.bash.dash.users.domain.RiderProfile;
import com.bash.dash.location.models.GeoPoint;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "driver_profile_id")
    private DriverProfile driverProfile;

    @ManyToOne
    @JoinColumn(name = "rider_profile_id")
    private RiderProfile riderProfile;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "lat",
                    column = @Column(name = "origin_lat")
            ),
            @AttributeOverride(
                    name = "lng",
                    column = @Column(name = "origin_lng")
            )
    })
    private GeoPoint origin;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(
                    name = "lat",
                    column = @Column(name = "destination_lat")
            ),
            @AttributeOverride(
                    name = "lng",
                    column = @Column(name = "destination_lng")
            )
    })
    private GeoPoint destinationPoint;

    private boolean isAvailable = true;
    private Status status = Status.REQUESTED;
}