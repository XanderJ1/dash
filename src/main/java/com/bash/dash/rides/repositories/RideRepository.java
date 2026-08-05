package com.bash.dash.rides.repositories;

import com.bash.dash.rides.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {
    Optional<Ride> findByUserId(Long id);

    Optional<Ride> findByIdAndDriverId(UUID rideId, Long userId);
}
