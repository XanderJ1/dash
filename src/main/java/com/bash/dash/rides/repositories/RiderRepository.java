package com.bash.dash.rides.repositories;

import com.bash.dash.domain.Rider;
import com.bash.dash.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RiderRepository extends JpaRepository<Rider, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);
}