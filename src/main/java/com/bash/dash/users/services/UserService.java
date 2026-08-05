package com.bash.dash.users.services;

import com.bash.dash.users.repositories.UserRepository;
import com.bash.dash.domain.User;
import com.bash.dash.users.dtos.UpdateDto;
import com.bash.dash.utils.MessageResponse;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public  UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
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

}
