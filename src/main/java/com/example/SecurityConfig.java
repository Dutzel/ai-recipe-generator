package com.example;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors() // CORS aktivieren
                .and()
                .csrf().disable() // CSRF-Schutz deaktivieren (optional)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/songtext").permitAll() // Erlaubt POST-Anfragen auf /songtext
                        .anyRequest().permitAll() // Erlaubt alle anderen Anfragen ohne Authentifizierung
                );

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:8080")); // Erlaubt die spezifischen Ursprünge
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Erlaubt die entsprechenden Methoden
        configuration.setAllowCredentials(true); // Erlaubt das Senden von Anmeldeinformationen (falls benötigt)
        configuration.setAllowedHeaders(Arrays.asList("*")); // Erlaubt alle Header

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // CORS für alle Endpunkte registrieren
        return source;
    }
}
