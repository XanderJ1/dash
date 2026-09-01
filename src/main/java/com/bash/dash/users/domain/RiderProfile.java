package com.bash.dash.users.domain;

import com.bash.dash.rides.models.Ride;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @OneToMany(mappedBy = "riderProfile")
    List<Ride> rides;
}
