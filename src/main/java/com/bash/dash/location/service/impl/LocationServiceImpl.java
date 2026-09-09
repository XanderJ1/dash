package com.bash.dash.location.service.impl;

import com.bash.dash.config.DriverLocationWebSocketService;
import com.bash.dash.config.MessageQueueConfig;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final DriverLocationWebSocketService sendDriverLocation;
    public LocationServiceImpl(LocationRepository locationRepository, DriverProfileRepository driverProfileRepository, RideRepository rideRepository, UserRepository userRepository, UserService userService, DriverLocationWebSocketService sendDriverLocation) {
        this.locationRepository = locationRepository;
        this.driverProfileRepository = driverProfileRepository;
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.sendDriverLocation = sendDriverLocation;
    }

    public Long getId(){
        return userService.getId();
    }

    @Cacheable(key = "#driverId", value = "location")
    public DriverLocation driverLocation(Long driverId){
        System.out.println("driverId: " + driverId  );
        User user = userRepository.findById(driverId).orElseThrow(() -> new RuntimeException("Driver does not exist"));
        log.info(String.valueOf(user.getDriverProfile() != null));
        DriverProfile driver = user.getDriverProfile();
        return locationRepository.findById(driver.getId())
                .orElseThrow(() -> new RuntimeException("Location of Driver does not exist"));
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
    @CacheEvict(key = "@userService.getId()", value = "location")
    public MessageResponse updateLocation(Double lat, Double lng, Long driverId) {

        DriverLocation driverLocation = new DriverLocation(driverId, new GeoPoint(lat, lng));
        driverLocation.setUpdatedAt(Instant.now());
        log.info(driverLocation.toString());
        locationRepository.save(driverLocation);

        sendDriverLocation.sendDriverLocation(
                driverId,
                lat,
                lng
        );
        return new MessageResponse("User location updated");
    }
}
