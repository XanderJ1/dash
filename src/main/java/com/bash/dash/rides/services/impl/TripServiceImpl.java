package com.bash.dash.rides.services.impl;

import com.bash.dash.users.domain.DriverProfile;
import com.bash.dash.users.domain.RiderProfile;
import com.bash.dash.drivers.repositories.RiderProfileRepository;
import com.bash.dash.location.models.GeoPoint;
import com.bash.dash.location.repositories.LocationRepository;
import com.bash.dash.users.domain.Role;
import com.bash.dash.users.domain.User;
import com.bash.dash.drivers.repositories.DriverProfileRepository;
import com.bash.dash.rides.dtos.RideResponseDto;
import com.bash.dash.users.repositories.UserRepository;
import com.bash.dash.authentication.models.CustomUserDetails;
import com.bash.dash.rides.dtos.RideRequestDto;
import com.bash.dash.rides.models.Ride;
import com.bash.dash.rides.models.Status;
import com.bash.dash.rides.repositories.RideRepository;
import com.bash.dash.rides.services.TripService;
import com.bash.dash.users.services.UserService;
import com.bash.dash.utils.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TripServiceImpl implements TripService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final DriverProfileRepository driverProfileRepository;
    private final RiderProfileRepository riderProfileRepository;
    private final UserService userService;

    public TripServiceImpl(RideRepository rideRepository, UserRepository userRepository, DriverProfileRepository driverProfileRepository, LocationRepository locationRepository, RiderProfileRepository riderProfileRepository, UserService userService) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.driverProfileRepository = driverProfileRepository;
        this.riderProfileRepository = riderProfileRepository;
        this.userService = userService;
    }

    public Long getId(){
        return userService.getId();
    }

    public List<RideResponseDto> fetchRides(){
        return rideRepository.findAll().stream().map(RideResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public List<RideResponseDto> fetchMyRides() {
        User user =  userRepository.findById(getId()).orElseThrow(()-> new RuntimeException("User not found"));
        return userService.getRole() == Role.DRIVER ? fetchDriverRides(user) : fetchRiderRides(user);
    }

    public List<RideResponseDto> fetchRiderRides(User user){
        return rideRepository.getRidesByRiderProfile(user.getRiderProfile()).stream()
                .map(RideResponseDto::new).collect(Collectors.toList());
    }

    public List<RideResponseDto> fetchDriverRides(User user){
        DriverProfile driverProfile = user.getDriverProfile();
        return rideRepository.getRidesByDriverProfile(driverProfile).stream().map(RideResponseDto::new).collect(Collectors.toList());
    }

    @Override
    public MessageResponse requestRide(RideRequestDto body) {
        log.info(String.valueOf(body));

        User rider =  userRepository.findById(getId()).orElseThrow(() -> new RuntimeException("User not found"));
        RiderProfile riderProfile = riderProfileRepository.findByUser(rider);

        log.info("RiderProfile: {}", riderProfile);
        log.info("RiderProfile ID: {}", riderProfile != null ? riderProfile.getId() : null);
        GeoPoint origin = body.origin();
        GeoPoint destination = body.destination();
        Ride ride = new Ride();
        ride.setRiderProfile(riderProfile);
        ride.setOrigin(origin);
        ride.setDestinationPoint(destination);
        rideRepository.save(ride);
        return new MessageResponse("Ride requested successfully");
    }

    @Override
    public MessageResponse acceptRide(String rideIdStr) {
        UUID rideId = UUID.fromString(rideIdStr);
        User driver = userRepository.findById(getId()).orElseThrow(() -> new RuntimeException("Could not find driver."));
        log.info("Driver ID: {}", driver.getId());
        log.info("Driver Name: {}", driver.getFirstName());
        DriverProfile driverProfile = driverProfileRepository.findByUser(driver);
        log.info("Driver Profile: {}", driverProfile);
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride is unavailable"));
        if (!ride.isAvailable()){
            return new MessageResponse("Ride is not available");
        }
        ride.setAvailable(false);
        ride.setDriverProfile(driverProfile);
        ride.setStatus(Status.ACCEPTED);
        driverProfile.setAvailable(false);
        rideRepository.save(ride);
        return new MessageResponse("Ride accepted");
    }

    @Override
    public MessageResponse completeRide(String rideId) {
        UUID id = UUID.fromString(rideId);
        Ride ride = rideRepository.findById(id).orElseThrow(() -> new RuntimeException(""));
        ride.setStatus(Status.COMPLETED);
        DriverProfile driver = driverProfileRepository.findById(ride.getDriverProfile().getId()).orElseThrow(() -> new RuntimeException("User not found"));
        driver.setAvailable(true);
        driverProfileRepository.save(driver);
        rideRepository.save(ride);
        return new  MessageResponse("Ride has been completed successfully");
    }

    public MessageResponse cancelRide(String rideIdStr){
        UUID rideId = UUID.fromString(rideIdStr);
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RuntimeException("Ride not found"));
        if (ride.getDriverProfile() != null){
            DriverProfile driver = driverProfileRepository.findById(ride.getDriverProfile().getId()).orElseThrow(() -> new RuntimeException("Driver does not exist"));
            driver.setAvailable(true);
            ride.setAvailable(false);
            ride.setStatus(Status.CANCELLED);
        }
        rideRepository.save(ride);
        return new MessageResponse("Ride cancelled successfully");
    }
}