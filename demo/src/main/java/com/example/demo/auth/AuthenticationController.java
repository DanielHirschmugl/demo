package com.example.demo.auth;

import com.example.demo.exceptions.UserAlreadyExistsException;
import com.example.demo.exceptions.UserNotFoundException;
import com.example.demo.exceptions.WrongInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) throws UserAlreadyExistsException {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @DeleteMapping("/deleteAccount")
    public ResponseEntity<String> deleteAccount(@RequestHeader("Authorization") String token) throws UserNotFoundException, WrongInputException {
        // Entferne das "Bearer " Präfix, falls vorhanden
        String jwt = token.startsWith("Bearer ") ? token.substring(7) : token;

        // Rufe die Service-Methode auf, um den Account zu löschen
        service.deleteAccountByToken(jwt);

        // Rückgabe der Erfolgsnachricht
        return ResponseEntity.ok("Account deleted successfully");
    }


    /*
    @PostMapping("/refreshToken")
    public ResponseEntity<AuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        //todo
    }

     */
}
