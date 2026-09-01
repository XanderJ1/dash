package com.bash.dash.drivers.repositories;

import com.bash.dash.users.domain.RiderProfile;
import com.bash.dash.users.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderProfileRepository extends JpaRepository<RiderProfile, Long> {

    public RiderProfile findByUser(User rider);
}