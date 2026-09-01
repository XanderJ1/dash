package com.bash.dash.location.controller;

import com.bash.dash.location.dtos.DriverLocationDto;
import com.bash.dash.location.models.DriverLocation;
import com.bash.dash.location.service.LocationService;
import com.bash.dash.utils.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/location")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PutMapping()
    public ResponseEntity<MessageResponse> updateLocation(@RequestBody DriverLocationDto driverLocation) {

        return ResponseEntity.ok(locationService.updateLocation(driverLocation.latitude(), driverLocation.longitude()));
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<DriverLocation> searchLocation(@PathVariable String rideId) {
        UUID uuid = UUID.fromString(rideId);
        return ResponseEntity.ok(locationService.driverLocation(uuid));
    }

    @GetMapping("/online")
    public ResponseEntity<MessageResponse> get() {
        return ResponseEntity.ok(locationService.toggleAvailable());
    }
}