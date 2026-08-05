package com.bash.dash.rides.dtos;

import com.bash.dash.location.models.Location;

public record RideRequestDto(
        Long driverId,
        Long userId,
        Location origin,
        Location destination
) {
}
