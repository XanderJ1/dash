package com.bash.dash.drivers.repositories;

import com.bash.dash.domain.DriverProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {
}