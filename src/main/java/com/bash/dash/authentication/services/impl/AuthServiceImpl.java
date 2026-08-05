package com.bash.dash.authentication.services.impl;

import com.bash.dash.authentication.dtos.JwtResponse;
import com.bash.dash.authentication.dtos.RegisterDto;
import com.bash.dash.authentication.models.*;
import com.bash.dash.users.repositories.UserRepository;
import com.bash.dash.authentication.repositories.VerificationCodeRepository;
import com.bash.dash.authentication.services.AuthService;
import com.bash.dash.authentication.services.JwtService;
import com.bash.dash.domain.*;
import com.bash.dash.email.EmailService;
import com.bash.dash.exceptions.Forbidden;
import com.bash.dash.utils.MessageResponse;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final VerificationCodeRepository codeRepository;


    public String getEmail(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        CustomUserDetails userDetails = (CustomUserDetails) principal;
        return userDetails.getEmail();
    }

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService, EmailService emailService, VerificationCodeRepository codeRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailService = emailService;
        this.codeRepository = codeRepository;
    }

    public String register(RegisterDto body) throws MessagingException {
        try{
            log.info(String.valueOf(body));
            Optional<User> originalUser = userRepository.findByEmail(body.email());
            //TODO If we use a link, the link will be only needed once.
            if (originalUser.isPresent()){
                if (originalUser.get().isEnabled()){
                    return "User already has an active account";
                }

                VerificationCode verificationCode = new VerificationCode();
                SecureRandom secureRandom = new SecureRandom();
                String code = String.format("%06d", secureRandom.nextInt(1_000_000));
                verificationCode.setCode(code);
                verificationCode.setExpiry(new Date(System.currentTimeMillis() + Long.parseLong("10000")));
                verificationCode.setEmail(body.email());
                codeRepository.save(verificationCode);
                log.info("Sending email to {}", body.email());
                emailService.send(body.email(), "Verify", "Enter this to verify your account" + code);
                return "User already exists. Enter the code to verify account";
            }


            User user = new User();

            if ("DRIVER".equals(body.role())){
                user = new Driver();
            }

            if ("RIDER".equals(body.role())){
                user = new Rider();
            }

            user.setFirstName(body.firstName());
            user.setLastName(body.lastName());
            user.setEmail(body.email());
            user.setPassword(passwordEncoder.encode(body.password()));
            user.setPhone(body.phone());
            user.setRole(Role.valueOf(body.role()));
            user.setDocument(body.document());
            userRepository.save(user);
            log.info("User created");

            VerificationCode verificationCode = new VerificationCode();
            SecureRandom secureRandom = new SecureRandom();
            String code = String.format("%06d", secureRandom.nextInt(1_000_000));
            verificationCode.setCode(code);
            verificationCode.setExpiry(new Date(System.currentTimeMillis() + Long.parseLong("10000")));
            verificationCode.setEmail(body.email());
            codeRepository.save(verificationCode);
            log.info("Sending email to {}", body.email());
            emailService.send(body.email(), "Registration", "Enter this to verify your account " + verificationCode.getCode());

            return "User created successfully";
        }catch (MailException e){
            log.info(String.valueOf(e));
            throw new RuntimeException("Error sending email to " + body.email());
        } catch (Exception e) {
            log.info(String.valueOf(e));
            throw new RuntimeException("Error creating user", e);
        }
    }


    public JwtResponse login(String email, String password) throws Forbidden {

        if (email == null || password == null){
            log.info("Enter username and password");
        }
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User does not exist"));
        if (!user.isEnabled()){
            throw new Forbidden("Verify account to login", "UNAUTHORIZED");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            log.info(String.valueOf(user));
            String token = jwtService.generateToken(authentication);
            return new JwtResponse(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getRole().name(),
                    token
            );
        }catch (BadCredentialsException ex){
            throw new BadCredentialsException("Invalid email or password");
        }catch (DisabledException ex){
            //TODO: Send a message to reverify a user

            throw new DisabledException("User is disabled");
        }
        catch (Exception ex){
            throw ex;
        }
    }


    public ResponseEntity<MessageResponse> verify(String otpCode, String email){

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Error: Could not retrieve user"));
        VerificationCode verificationCode = codeRepository.findByEmailAndCode(email, otpCode)
                 .orElseThrow(() -> new RuntimeException("Verification code doesn't exist"));
        if (!verificationCode.getCode().equals(otpCode)){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Verification code is incorrect"));
        }
        if (verificationCode.getExpiry().after(new Date())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Verification code is expired"));
        }
        user.setEnabled(true);
        userRepository.save(user);
        return ResponseEntity.ok(new MessageResponse("User account enabled"));
    }
}