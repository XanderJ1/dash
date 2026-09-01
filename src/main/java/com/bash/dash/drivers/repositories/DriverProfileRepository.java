package com.bash.dash.drivers.repositories;

import com.bash.dash.users.domain.DriverProfile;
import com.bash.dash.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverProfileRepository extends JpaRepository<DriverProfile, Long> {

    public DriverProfile findByUser(User driverId);

}