package com.bash.dash.rides.services.impl;

import com.bash.dash.messaging.RideProducer;
import com.bash.dash.messaging.dtos.RideMessage;
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
import com.bash.dash.rides.dtos.RideRequestDto;
import com.bash.dash.rides.models.Ride;
import com.bash.dash.rides.models.Status;
import com.bash.dash.rides.repositories.RideRepository;
import com.bash.dash.rides.services.TripService;
import com.bash.dash.users.services.UserService;
import com.bash.dash.utils.MessageResponse;
import lombok.extern.slf4j.Slf4j;
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
    private final RideProducer rideProducer;

    public TripServiceImpl(RideRepository rideRepository, UserRepository userRepository, DriverProfileRepository driverProfileRepository, LocationRepository locationRepository, RiderProfileRepository riderProfileRepository, UserService userService, RideProducer rideProducer) {
        this.rideRepository = rideRepository;
        this.userRepository = userRepository;
        this.driverProfileRepository = driverProfileRepository;
        this.riderProfileRepository = riderProfileRepository;
        this.userService = userService;
        this.rideProducer = rideProducer;
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
        User rider =  userRepository.findById(getId()).orElseThrow(() ->
                new RuntimeException("User not found"));
        RiderProfile riderProfile = riderProfileRepository.findByUser(rider);

        log.info("User ID: {}", rider.getId());

        if (riderProfile == null) {
            log.error("No RiderProfile found for User ID: {}", rider.getId());
            throw new RuntimeException("Rider profile not found");
        }

        GeoPoint origin = body.origin();
        GeoPoint destination = body.destination();
        Ride ride = new Ride();
        ride.setRiderProfile(riderProfile);
        ride.setOrigin(origin);
        ride.setDestinationPoint(destination);
        log.info(
                "Ride before save - RiderProfile ID: {}",
                ride.getRiderProfile().getId()
        );

        rideRepository.save(ride);
        return new MessageResponse("Ride requested successfully");
    }

    @Override
    public MessageResponse acceptRide(String rideIdStr) {
        UUID rideId = UUID.fromString(rideIdStr);
        User driver = userRepository.findById(getId()).orElseThrow(() ->
                new RuntimeException("Could not find driver."));
        DriverProfile driverProfile = driverProfileRepository.findByUser(driver);
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
        if (!ride.isAvailable()){
            return new MessageResponse("Ride is not available");
        }
        ride.setAvailable(false);
        ride.setDriverProfile(driverProfile);
        ride.setStatus(Status.ACCEPTED);
        driverProfile.setAvailable(false);
        rideRepository.save(ride);
        // TODO: Start tracking the driver
        RideMessage rideMessage = new RideMessage(
                rideId, driver.getId(), ride.getStatus(),
                ride.getRiderProfile().getId()
        );

        rideProducer.sendMessage(rideMessage);
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
        }
        ride.setAvailable(false);
        ride.setStatus(Status.CANCELLED);
        rideRepository.save(ride);
        return new MessageResponse("Ride cancelled successfully");
    }
}