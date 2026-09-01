package com.bash.dash.authentication.dtos;

import com.bash.dash.users.domain.RiderProfile;

public record RiderProfileDto(
        Long id,
        Long riderId,
        String name


) {

    public RiderProfileDto(RiderProfile profile){
        this(
                profile != null ? profile.getId() : null,
                profile != null ? profile.getUser().getId(): null,
                profile != null ? profile.getUser().getFirstName(): null
        );
    }

}
