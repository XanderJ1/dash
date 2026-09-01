package com.bash.dash.config.security;

import com.bash.dash.authentication.dtos.JwtResponse;
import com.bash.dash.users.domain.Role;
import com.bash.dash.users.domain.User;
import com.bash.dash.users.repositories.UserRepository;
import com.bash.dash.authentication.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String firstName = oAuth2User.getAttribute("given_name");
        String lastName = oAuth2User.getAttribute("family_name");

        if (email == null) {
            response.sendRedirect("/login?error=no_email");
            return;
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setRole(Role.RIDER);
                    newUser.setEnabled(true);
                    return userRepository.save(newUser);
                });

        String token = jwtService.generateToken(authentication);

        // Option 1: Redirect to frontend with token as query parameter
        // Adjust this URL to match your frontend
//        String redirectUrl = "http://localhost:3000/oauth/callback?token=" + token;
        
        // Option 2: Return JSON response (uncomment if you prefer this)
        JwtResponse jwtResponse = new JwtResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().name(),
                token
        );

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(jwtResponse));

    }
}