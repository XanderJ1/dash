package com.bash.dash.authentication.services;

import com.bash.dash.domain.User;
import com.bash.dash.users.repositories.UserRepository;
import com.bash.dash.authentication.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        User user;
        if (identifier.contains("@")){
            user = userRepository.findByEmail(identifier).orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return CustomUserDetails.build(user);
        }

        user = userRepository.findByPhone(identifier).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return CustomUserDetails.build(user);
    }

}
