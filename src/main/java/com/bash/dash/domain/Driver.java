package com.bash.dash.domain;

import com.bash.dash.rides.models.Ride;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Entity
@Data
public class Driver extends User{

    private boolean isAvailable;
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "ride_id")
    private List<Ride> rides;

    public Driver() {

    }
}
