package com.bash.dash.drivers.repositories;

import com.bash.dash.domain.RiderProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderProfileRepository extends JpaRepository<RiderProfile, Long> {
}