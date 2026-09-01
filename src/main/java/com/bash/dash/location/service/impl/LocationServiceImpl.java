package com.bash.dash.location.service.impl;

import com.bash.dash.authentication.models.CustomUserDetails;
import com.bash.dash.drivers.repositories.DriverProfileRepository;
import com.bash.dash.rides.models.Ride;
import com.bash.dash.rides.models.Status;
import com.bash.dash.rides.repositories.RideRepository;
import com.bash.dash.users.domain.DriverProfile;
import com.bash.dash.users.domain.User;
import com.bash.dash.location.models.DriverLocation;
import com.bash.dash.location.models.GeoPoint;
import com.bash.dash.location.repositories.LocationRepository;
import com.bash.dash.location.service.LocationService;
import com.bash.dash.users.repositories.UserRepository;
import com.bash.dash.users.services.UserService;
import com.bash.dash.utils.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    public LocationServiceImpl(LocationRepository locationRepository, DriverProfileRepository driverProfileRepository, RideRepository rideRepository, UserRepository userRepository, UserService userService) {
        this.locationRepository = locationRepository;
        this.driverProfileRepository = driverProfileRepository;
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public Long getId(){
        return userService.getId();
    }

    @Cacheable(value = "location",key = "#rideId")
    public DriverLocation driverLocation(UUID rideId){
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride with id: " + rideId + " not found"));

        return locationRepository.findById(ride.getDriverProfile().getId())
                .orElseThrow(() -> new RuntimeException("Ride with id: " + rideId + " not found"));
    }

    @Override
    public MessageResponse toggleAvailable() {
        User user = userRepository.findById(getId()).orElseThrow(() -> new RuntimeException("User not found"));
        DriverProfile driver = driverProfileRepository.findByUser(user);
        Ride lastRide = driver.getRides().getLast();
        if (lastRide.getStatus() == Status.ACCEPTED || lastRide.getStatus() == Status.EN_ROUTE || lastRide.getStatus() ==Status.EN_ROUTE ){
            return new MessageResponse("Driver is in transit");
        }

        driver.setAvailable(!driver.isAvailable());
        driverProfileRepository.save(driver);
        return new MessageResponse("User availability toggled.");
    }

    @Override
    public MessageResponse updateLocation(Double lat, Double lng) {

        User user = userRepository.findById(getId()).orElseThrow(() -> new RuntimeException("User not found"));
        DriverLocation driverLocation = new DriverLocation(user.getId(), new GeoPoint(lat, lng));
        driverLocation.setUpdatedAt(Instant.now());
        log.info(driverLocation.toString());
        locationRepository.save(driverLocation);
        return new MessageResponse("User location updated");
    }
}


