package com.bash.dash.rides.dtos;

import com.bash.dash.location.models.GeoPoint;

public record RideRequestDto(
        Long driverId,
        Long userId,
        GeoPoint origin,
        GeoPoint destination
) {
}
