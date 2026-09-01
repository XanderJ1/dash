package com.bash.dash.users.services;

import com.bash.dash.authentication.models.CustomUserDetails;
import com.bash.dash.users.domain.Role;
import com.bash.dash.users.repositories.UserRepository;
import com.bash.dash.users.domain.User;
import com.bash.dash.users.dtos.UpdateDto;
import com.bash.dash.utils.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public  UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long getId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null){
            return 0L;
        }

        Object principal = authentication.getPrincipal();
        CustomUserDetails userDetails = null;
        if (principal instanceof CustomUserDetails){
            userDetails = (CustomUserDetails) principal;
            return userDetails.getId();
        }else {
            assert principal != null;
            throw new RuntimeException("Unexpected principal type: " + principal.getClass());
        }
    }

    public Role getRole() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails userDetails) {
            return userDetails.getRole();
        }

        throw new RuntimeException(
                "Unexpected principal type: " + principal.getClass()
        );
    }

    public List<User> findAll() {
        log.info("Find All Users");
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public MessageResponse update(UpdateDto body, Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (body.firstName() != null){
            user.setFirstName(body.firstName());
        }
        if (body.lastName() != null){
            user.setLastName(body.lastName());
        }
        if(body.email() != null){
            user.setEmail(body.email());
        }
        if (body.address() != null){
            user.setAddress(body.address());
        }
        if (body.password() != null){
            user.setPassword(passwordEncoder.encode(body.password()));
        }

        userRepository.save(user);
        return new MessageResponse("User updated successfully");
    }

    public MessageResponse delete(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User does not exist"));
        userRepository.delete(user);
        return new MessageResponse("User deleted");
    }

    public User findMe() {

        return userRepository.findById(getId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
