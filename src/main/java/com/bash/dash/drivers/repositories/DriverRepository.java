package com.bash.dash.drivers.repositories;

import com.bash.dash.domain.Driver;
import com.bash.dash.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);
}