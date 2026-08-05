package com.bash.dash.rides.services;

import com.bash.dash.rides.dtos.RideRequestDto;
import com.bash.dash.utils.MessageResponse;
import org.springframework.stereotype.Service;

@Service
public interface RideMatchingService {

    public MessageResponse updateLocation(Double lat, Double lng);
    public MessageResponse requestRide(RideRequestDto body);
    public MessageResponse acceptRide(String rideId, boolean accept);

    public MessageResponse toggleAvailable();

    MessageResponse completedRide(String rideId, boolean b);
}

