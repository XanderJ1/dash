package com.bash.dash.authentication.dtos;

public record JwtResponse(
        Long id,
        String firstName,
        String lastName,
        String role,
        String email,
        String token
){
}
