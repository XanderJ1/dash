package com.bash.dash.rides.controllers;

import com.bash.dash.rides.dtos.RideRequestDto;
import com.bash.dash.rides.dtos.RideResponseDto;
import com.bash.dash.rides.services.TripService;
import com.bash.dash.users.services.UserService;
import com.bash.dash.utils.MessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rides")
public class RideController {

    private final TripService tripService;
    private final UserService userService;

    public RideController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @GetMapping("")
//    @PreAuthorize("hasRole('ADMIN')")
    public List<RideResponseDto> fetchRides(){
        return tripService.fetchRides();
    }

    @GetMapping("/me")
    public List<RideResponseDto> fetchRideHistory(){
        userService.getId();
        return tripService.fetchMyRides();
    }

    @PostMapping("/request")
    public ResponseEntity<MessageResponse> requestRide(@RequestBody RideRequestDto rideRequest){
        return ResponseEntity.ok(tripService.requestRide(rideRequest));
    }

    @PostMapping("/accept")
    public ResponseEntity<MessageResponse> acceptRide(@RequestParam String rideId){
        return ResponseEntity.ok(tripService.acceptRide(rideId));
    }

    @PostMapping("/complete")
    public ResponseEntity<MessageResponse> completeRide(@RequestParam String rideId){
        return ResponseEntity.ok(tripService.completeRide(rideId));
    }

    @PostMapping("/cancel")
    public ResponseEntity<MessageResponse> cancel(@RequestParam String rideId){
        return ResponseEntity.ok(tripService.cancelRide(rideId));
    }

}
