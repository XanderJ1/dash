package com.bash.dash.rides.services;

import com.bash.dash.rides.dtos.RideRequestDto;
import com.bash.dash.rides.dtos.RideResponseDto;
import com.bash.dash.utils.MessageResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TripService {

    public List<RideResponseDto> fetchRides();

    public List<RideResponseDto> fetchMyRides();
    public MessageResponse requestRide(RideRequestDto body);
    public MessageResponse acceptRide(String rideId);

    MessageResponse completeRide(String rideId);

    MessageResponse cancelRide(String rideId);
}

