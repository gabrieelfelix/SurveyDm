package com.example.fieldpolling.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.fieldpolling.dtos.AuthenticationDTO;
import com.example.fieldpolling.dtos.LoginResponseDTO;
import com.example.fieldpolling.dtos.RegisterDTO;
import com.example.fieldpolling.helpers.OnRegistrationCompleteEvent;
import com.example.fieldpolling.infra.security.TokenService;
import com.example.fieldpolling.models.User;
import com.example.fieldpolling.models.VerificationToken;
import com.example.fieldpolling.repositories.TokenRepository;
import com.example.fieldpolling.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Calendar;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    ApplicationEventPublisher eventPublisher;
    @Autowired
    private TokenRepository tokenRepository;

    @SuppressWarnings("rawtypes")
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        // return ResponseEntity.ok(new LoginResponseDTO(token));
        return ResponseEntity.ok(new LoginResponseDTO(token, (User) repository.findByUsername(data.username())));
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data, HttpServletRequest request) {
        if (this.repository.findByUsername(data.username()) != null)
            return ResponseEntity.badRequest().body("An account for that username already exists.");
        if (this.repository.findByEmail(data.email()) != null)
            return ResponseEntity.badRequest().body("An account for that email already exists.");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.email(), data.username(), encryptedPassword, data.role());

        try {
            this.repository.save(newUser);
            eventPublisher.publishEvent(
                    new OnRegistrationCompleteEvent(newUser, request.getLocale(), request.getContextPath()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error on saving/sending email" + e);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<Object> confirmRegistration
    (@RequestParam("token") String token) {
        
        VerificationToken verificationToken = tokenRepository.findByToken(token);

        if (verificationToken == null) {
            return ResponseEntity.badRequest().body("nullToken");
        }
        
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            return ResponseEntity.badRequest().body("Token Expired");        
        } 
        
        user.setEnabled(true); 
        return ResponseEntity.ok().build(); 
    }
}