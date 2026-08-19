package com.bash.dash.domain;

import com.bash.dash.rides.models.Ride;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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

    @OneToMany(mappedBy = "driverProfile")
    private List<Ride> rides;

}
