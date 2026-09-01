package com.bash.dash.authentication.controllers;

import com.bash.dash.authentication.dtos.JwtResponse;
import com.bash.dash.authentication.dtos.LoginDto;
import com.bash.dash.authentication.dtos.RegisterDto;
import com.bash.dash.authentication.services.AuthService;
import com.bash.dash.exceptions.Forbidden;
import com.bash.dash.utils.MessageResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/auth")
@RestController
public class  AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(@RequestBody RegisterDto body) throws MessagingException {
        MessageResponse response = new MessageResponse(authService.register(body));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginDto body) throws Forbidden {

        JwtResponse response = authService.login(body.email(), body.password());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify")
    public ResponseEntity<MessageResponse> verify(@RequestParam String token) throws Forbidden {
        return authService.verify(token);
    }

}