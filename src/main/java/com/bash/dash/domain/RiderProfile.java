package com.bash.dash.domain;

import com.bash.dash.rides.models.Ride;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class RiderProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private User user;


    @OneToMany(mappedBy = "riderProfile")
    List<Ride> rides;
}
