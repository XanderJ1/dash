package com.bash.dash.location.repositories;

import com.bash.dash.domain.User;
import com.bash.dash.location.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Optional<Location> findById(User user);

}
