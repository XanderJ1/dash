package com.bash.dash.authentication.dtos;

import com.bash.dash.users.domain.DriverProfile;

public record DriverProfileDto(
        Long id,
        String phone,
        boolean isAvailable,
        String name,
        Long userId


        ) {
    public DriverProfileDto(DriverProfile profile){
        this(
                profile != null ? profile.getId() : null,
                profile != null ? profile.getPhone() : null,
                profile != null && profile.isAvailable(),
                profile != null && profile.getUser() != null ? profile.getUser().getFirstName(): null,
                profile != null && profile.getUser() != null ? profile.getUser().getId(): null
        );
    }
}