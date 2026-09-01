package com.bash.dash.location.service;

import com.bash.dash.location.models.DriverLocation;
import com.bash.dash.utils.MessageResponse;

import java.util.UUID;

public interface LocationService {

    public MessageResponse updateLocation(Double lat, Double lng);

    public DriverLocation driverLocation(UUID rideId);

    public MessageResponse toggleAvailable();

}
