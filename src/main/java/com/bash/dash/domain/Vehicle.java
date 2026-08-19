package com.bash.dash.domain;

import jakarta.persistence.*;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private DriverProfile rider;
    private String vehicleName;
    private String licenseExpiry;
    private String licensePlate;
    private String driverId;
}