package com.bash.dash.drivers.controllers;

import com.bash.dash.location.service.LocationService;
import com.bash.dash.rides.services.TripService;
import com.bash.dash.utils.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DriverController {

    private final LocationService tripService;

    public DriverController(LocationService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> toggleAvailable(){
        return ResponseEntity.ok(tripService.toggleAvailable());
    }
}

