package com.bash.dash.users.domain;

import jakarta.persistence.*;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private DriverProfile driverProfile;
    private String vehicleName;
    private String vehicleType;
    private String vehicleColor;
    private String modelName;
    private String licenseExpiry;
    private String licensePlate;
    private String driverId;
}