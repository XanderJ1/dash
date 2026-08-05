package com.bash.dash.authentication.dtos;

import com.bash.dash.domain.Document;
import com.bash.dash.domain.User;

public record RegisterDto(
    String firstName,
    String lastName,
    String email,
    String phone,
    String password,
    String role,
    String address,
    Document document
) {
public RegisterDto(User user){
    this(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhone(),
            user.getPassword(),
            user.getAddress(),
            user.getAddress(),
            user.getDocument()
    );
}
}
