package com.bash.dash.rides.repositories;

import com.bash.dash.rides.dtos.RideResponseDto;
import com.bash.dash.rides.models.Ride;
import com.bash.dash.users.domain.DriverProfile;
import com.bash.dash.users.domain.RiderProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RideRepository extends JpaRepository<Ride, UUID> {

    public List<Ride> getRidesByRiderProfile(RiderProfile riderProfile);

    public List<Ride> getRidesByDriverProfile(DriverProfile driverProfile);
}
