package com.bash.dash.authentication.services;

import com.bash.dash.authentication.dtos.JwtResponse;
import com.bash.dash.authentication.dtos.RegisterDto;
import com.bash.dash.exceptions.Forbidden;
import com.bash.dash.utils.MessageResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    public String getEmail();

    public String register(RegisterDto body) throws MessagingException;

    public JwtResponse login(String email, String password) throws Forbidden;
    public ResponseEntity<MessageResponse> verify(String otpCode, String email);


}
