package com.bash.dash.drivers.controllers;

import com.bash.dash.rides.services.RideMatchingService;
import com.bash.dash.utils.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public class DriverController {

    private final RideMatchingService rideMatchingService;

    public DriverController(RideMatchingService rideMatchingService) {
        this.rideMatchingService = rideMatchingService;
    }

    @PostMapping
    public ResponseEntity<MessageResponse> toggleAvailable(){
        return ResponseEntity.ok(rideMatchingService.toggleAvailable());
    }}
