package com.bash.dash.users.dtos;

public record UpdateDto(
        String firstName,
        String lastName,
        String password,
        String email,
        String address
) {
}
