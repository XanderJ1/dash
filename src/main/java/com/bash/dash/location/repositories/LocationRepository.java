package com.bash.dash.location.repositories;

import com.bash.dash.location.models.DriverLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<DriverLocation, Long> {

//    Optional<DriverLocation> findByDriverProfileId(User user);

}