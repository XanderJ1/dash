package com.bash.dash.domain;

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
    private String password;
    private boolean isEnabled;
    private String address;

    @PrePersist()
    private void onCreate() {
        createdAt = new Date();
    }

    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private Role role;
}
