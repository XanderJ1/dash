package com.bash.dash.rides.services.impl;

import com.bash.dash.domain.Driver;
import com.bash.dash.location.repositories.LocationRepository;
import com.bash.dash.location.models.Location;
import com.bash.dash.domain.User;
import com.bash.dash.drivers.repositories.DriverRepository;
import com.bash.dash.users.repositories.UserRepository;
import com.bash.dash.authentication.models.CustomUserDetails;
import com.bash.dash.rides.dtos.RideRequestDto;
import com.bash.dash.rides.models.Ride;
import com.bash.dash.rides.models.Status;
import com.bash.dash.rides.repositories.RideRepository;
import com.bash.dash.rides.services.RideMatchingService;
import com.bash.dash.utils.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
public class RideMatchingServiceImpl implements RideMatchingService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final LocationRepository locationRepository;

    public RideMatchingServiceImpl(RideRepository rideRepository, UserRepository userRepository, DriverRepository driverRepository, LocationRepository locationRepository) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.locationRepository = locationRepository;
    }

    public Long getId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return 0L;
        }

        Object principal = authentication.getPrincipal();
        CustomUserDetails userDetails = null;
        if (principal instanceof CustomUserDetails){
            userDetails = (CustomUserDetails) principal;
            return userDetails.getId();
        }else {
            assert principal != null;
            throw new RuntimeException("Unexpected principal type: " + principal.getClass());
        }
    }

    @Override
    public MessageResponse updateLocation(Double lat, Double lng) {

        User user = userRepository.findById(getId()).orElseThrow(() -> new RuntimeException("User not found"));
        Location location = new Location(lat, lng);
        locationRepository.save(location);
        user.setLocation(location);
        userRepository.save(user);
        return new MessageResponse("User location updated");
    }

    @Override
    public MessageResponse requestRide(RideRequestDto body) {
        log.info(String.valueOf(body));

        Location origin = body.origin();
        Location destination = body.destination();
        locationRepository.save(origin);
        locationRepository.save(destination);
        Ride ride = new Ride();
        ride.setUserId(getId());
        ride.setOrigin(origin);
        ride.setDestination(destination);
        rideRepository.save(ride);
        return new MessageResponse("Ride requested successfully");
    }

    @Override
    public MessageResponse acceptRide(String  rideIdStr, boolean accept) {
        UUID rideId = UUID.fromString(rideIdStr);
        Driver driver = driverRepository.findById(getId()).orElseThrow(() -> new RuntimeException("Could not driver."));
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride is unavailable"));
        if (!ride.isAvailable()){
            return new MessageResponse("Ride is not available");
        }
        if (accept){
            ride.setAvailable(false);
            ride.setDriverId(getId());
            ride.setStatus(Status.ACCEPTED);
            rideRepository.save(ride);
            driver.setAvailable(false);
            return new MessageResponse("Ride accepted");
        }
        return new MessageResponse("Ride declined");
    }

    @Override
    public MessageResponse toggleAvailable() {
        Driver driver = (Driver) userRepository.findById(getId()).orElseThrow(() -> new RuntimeException("User not found"));
        Ride lastRide = driver.getRides().getLast();
        if (lastRide.getStatus() == Status.ACCEPTED || lastRide.getStatus() == Status.EN_ROUTE || lastRide.getStatus() ==Status.IN_TRANSIT ){
            return new MessageResponse("Driver is in transit");
        }

        driver.setAvailable(!driver.isAvailable());
        driverRepository.save(driver);
        return new MessageResponse("User availability toggled.");
    }

    @Override
    public MessageResponse completedRide(String rideId, boolean b) {

        UUID id = UUID.fromString(rideId);
        Ride ride = rideRepository.findById(id).orElseThrow(() -> new RuntimeException(""));
        Driver driver = (Driver) userRepository.findById(ride.getDriverId()).orElseThrow(() -> new RuntimeException("User not found"));
        driver.setAvailable(true);
        driverRepository.save(driver);
        return new  MessageResponse("Ride has been completed successfully");
    }


    public MessageResponse cancelRide(String rideIdStr){
        UUID rideId = UUID.fromString(rideIdStr);
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));
        Driver driver = driverRepository.findById(ride.getDriverId()).orElseThrow(() -> new RuntimeException("Driver does not exist"));
        driver.setAvailable(true);
        ride.setAvailable(false);
        ride.setStatus(Status.CANCELLED);
        return new MessageResponse("Ride cancelled successfully");
    }
}