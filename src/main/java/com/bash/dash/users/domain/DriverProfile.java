package com.bash.dash.users.domain;

import com.bash.dash.rides.models.Ride;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class DriverProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User user;

    private boolean isAvailable;

    @OneToOne
    private Vehicle vehicle;

    private String phone;

    @JsonIgnore
    @OneToMany(mappedBy = "driverProfile")
    private List<Ride> rides;

}
