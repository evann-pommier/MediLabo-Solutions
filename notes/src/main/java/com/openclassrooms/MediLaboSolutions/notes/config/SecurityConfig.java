package com.openclassrooms.MediLaboSolutions.notes.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration Spring Security de notes-service.
 * <p>
 * Sécurise l'accès aux endpoints /notes/** par authentification HTTP Basic,
 * avec un utilisateur unique en mémoire (pas d'inscription ni de gestion de
 * droits, conformément au besoin exprimé par le client). Cet utilisateur
 * correspond à celui utilisé par la gateway (filtre AddRequestHeader) et par
 * risk-service pour s'authentifier lors de leurs appels vers ce service.
 */
@Configuration
public class SecurityConfig {

    @Value("${security.user.name}")
    private String username;

    @Value("${security.user.password}")
    private String password;

    /** Encodage BCrypt du mot de passe — jamais stocké ni comparé en clair. */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Utilisateur unique en mémoire, chargé depuis application.properties
     * plutôt que codé en dur, pour permettre de changer les identifiants
     * sans recompiler (et pour rester cohérent entre les profils
     * local/docker).
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        return new InMemoryUserDetailsManager(
                User.withUsername(username)
                        .password(encoder.encode(password))
                        .roles("GATEWAY")
                        .build()
        );
    }

    /**
     * Définit la politique d'accès : /notes/** exige une authentification,
     * tout le reste est ouvert. CSRF désactivé car cette API REST est
     * consommée par d'autres services (gateway, risk-service), pas par un
     * navigateur avec formulaire HTML.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/notes/**").authenticated()
                        .anyRequest().permitAll())
                .httpBasic(basic -> {});
        return http.build();
    }
}