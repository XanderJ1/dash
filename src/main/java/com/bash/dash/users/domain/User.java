package com.bash.dash.users.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    @JsonIgnore
    private String password;
    private boolean isEnabled;
    private String address;

    // @JsonIgnore breaks the User <-> RiderProfile/DriverProfile JSON cycle:
    // RiderProfile/DriverProfile already carry a `user` reference back, so
    // this direction isn't needed in a response body, and without it any
    // endpoint serializing a User with a profile recurses infinitely
    // (StackOverflowError -> 500).
    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private RiderProfile riderProfile;

    @JsonIgnore
    @OneToOne(mappedBy = "user")
    private DriverProfile driverProfile;

    @PrePersist()
    private void onCreate() {
        createdAt = new Date();
    }

    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private Role role;
}
