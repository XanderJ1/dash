package com.bash.dash.rides.models;

import com.bash.dash.location.models.Location;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Long driverId;
    private Long userId;
    @OneToOne
    @JoinColumn(name = "origin_id")
    private Location origin;
    @OneToOne
    private Location destination;
    private boolean isAvailable = true;
    private Status status;
}