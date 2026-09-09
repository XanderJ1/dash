package com.bash.dash.messaging.dtos;

import com.bash.dash.rides.models.Status;

import java.util.UUID;

public record RideMessage(
        UUID rideId,
        Long driverId,
        Status status,
        Long riderId
) {
}
