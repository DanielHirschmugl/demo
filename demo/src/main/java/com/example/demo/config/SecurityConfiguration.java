package com.example.demo.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CSRF deaktivieren, da es nicht in einer stateless JWT-Anwendung benötigt wird
                .csrf(csrf -> csrf.disable())

                // Berechtigungen für HTTP-Anfragen festlegen
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/v1/auth/**").permitAll() // Endpunkte für Authentifizierung öffentlich machen
                        .anyRequest().authenticated() // Alle anderen Endpunkte erfordern Authentifizierung
                )

                // Session-Management auf stateless setzen, da JWT verwendet wird
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Benutzerdefinierter AuthenticationProvider für die Authentifizierung
                .authenticationProvider(authenticationProvider)

                // JWT-Filter vor den UsernamePasswordAuthenticationFilter setzen
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
