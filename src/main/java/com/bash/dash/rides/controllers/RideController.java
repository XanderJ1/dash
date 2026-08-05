package com.bash.dash.rides.controllers;

import com.bash.dash.rides.dtos.RideRequestDto;
import com.bash.dash.rides.models.Ride;
import com.bash.dash.rides.services.RideMatchingService;
import com.bash.dash.utils.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final RideMatchingService rideMatchingService;

    public RideController(RideMatchingService rideMatchingService) {
        this.rideMatchingService = rideMatchingService;
    }

    @GetMapping("")
    public List<Ride> fetchRides(){
        return new ArrayList<>();
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("Hello World");
    }

    @PutMapping("")
    public ResponseEntity<MessageResponse> updateLocation(@RequestParam Double lat, @RequestParam Double lng){
        return ResponseEntity.ok(rideMatchingService.updateLocation(lat, lng));
    }

    @PostMapping("/request")
    public ResponseEntity<MessageResponse> requestRide(@RequestBody RideRequestDto rideRequest){
        return ResponseEntity.ok(rideMatchingService.requestRide(rideRequest));
    }

    @PostMapping("/accept")
    public ResponseEntity<MessageResponse> acceptRide(@RequestParam String rideId,@RequestParam boolean b){
        return ResponseEntity.ok(rideMatchingService.acceptRide(rideId, b));
    }

    @PostMapping("/completed")
    public ResponseEntity<MessageResponse> completeRide(@RequestParam String rideId,@RequestParam boolean b){
        return ResponseEntity.ok(rideMatchingService.completedRide(rideId, b));
    }

}
