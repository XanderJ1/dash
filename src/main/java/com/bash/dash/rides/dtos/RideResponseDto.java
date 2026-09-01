package com.bash.dash.rides.dtos;

import com.bash.dash.authentication.dtos.DriverProfileDto;
import com.bash.dash.authentication.dtos.RiderProfileDto;
import com.bash.dash.location.models.DriverLocation;
import com.bash.dash.location.models.GeoPoint;
import com.bash.dash.rides.models.Ride;
import com.bash.dash.rides.models.Status;

import java.util.UUID;

public record RideResponseDto(
        UUID rideId,
        boolean available,
        GeoPoint origin,
        GeoPoint destination,
        DriverProfileDto driverProfile,
        RiderProfileDto riderProfile,
        Status status
) {

    public RideResponseDto(Ride ride){

        this(
                ride.getId(),
                ride.isAvailable(),
                ride.getOrigin(),
                ride.getDestinationPoint(),
                new DriverProfileDto(ride.getDriverProfile()),
                new RiderProfileDto(ride.getRiderProfile()),
                ride.getStatus()
        );
    }

}