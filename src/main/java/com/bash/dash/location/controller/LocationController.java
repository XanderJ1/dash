package com.bash.dash.location.controller;

import com.bash.dash.authentication.models.CustomUserDetails;
import com.bash.dash.messaging.dtos.LocationMessage;
import com.bash.dash.messaging.LocationProducer;
import com.bash.dash.location.dtos.DriverLocationDto;
import com.bash.dash.location.models.DriverLocation;
import com.bash.dash.location.service.LocationService;
import com.bash.dash.users.services.UserService;
import com.bash.dash.utils.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequestMapping("/api/v1/location")
public class LocationController {

    private final LocationService locationService;
    private final LocationProducer locationProducer;
    private final UserService userService;

    public LocationController(LocationService locationService, LocationProducer locationProducer, UserService userService) {
        this.locationService = locationService;
        this.locationProducer = locationProducer;
        this.userService = userService;
    }

    @PutMapping()
    public ResponseEntity<MessageResponse> updateLocation(@RequestBody DriverLocationDto driverLocation) {

        locationProducer.sendMessage(new LocationMessage(driverLocation.latitude(), driverLocation.longitude(), userService.getId()));
        return ResponseEntity.ok(new MessageResponse("User location updated"));
    }

    @GetMapping("")
    public ResponseEntity<DriverLocation> searchLocation(@RequestParam Long driverId) {
        var driverLocation = locationService.driverLocation(driverId);
        return ResponseEntity.ok(driverLocation);
    }

    @GetMapping("/online")
    public ResponseEntity<MessageResponse> get() {
        return ResponseEntity.ok(locationService.toggleAvailable());
    }

    @MessageMapping("/driver/location")
    public void updateDriverLocation(LocationMessage message, Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        Long driverId = customUserDetails.getId();

        locationProducer.sendMessage(
                new LocationMessage(message.lat(), message.lng(), driverId));
    }
}