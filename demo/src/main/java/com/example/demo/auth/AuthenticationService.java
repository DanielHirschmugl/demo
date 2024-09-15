package com.example.demo.auth;

import com.example.demo.config.JwtService;
import com.example.demo.exceptions.UserAlreadyExistsException;
import com.example.demo.exceptions.WrongInputException;
import com.example.demo.user.Role;
import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) throws UserAlreadyExistsException {
        var user = User.builder() //var: undefinierter Datentyp. Der Compiler erkennt zur Laufzeit
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();//Builder und build: builder am Anfang, build für den Abschluss, kommt von @Build
        if(repository.findByEmail(user.getEmail()).isPresent()) throw new UserAlreadyExistsException("Sie haben bereits einen Account.");
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()

                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public String deleteByEmail(AuthenticationRequest request) throws WrongInputException {
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new WrongInputException("Email oder Passwort ist falsch"));

        repository.deleteById(user.getId());

        return "Benutzer erfolgreich gelöscht";
    }
}
