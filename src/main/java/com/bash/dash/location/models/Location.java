package com.bash.dash.location.models;

import com.bash.dash.domain.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
public class Location{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double lat;
    private Double lng;
    @OneToOne
    private User user;

    public Location(Double lat, Double lng){
        this.lat = lat;
        this.lng = lng;
    }

}
