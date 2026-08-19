package com.bash.dash.rides.repositories;

import com.bash.dash.rides.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {
}
