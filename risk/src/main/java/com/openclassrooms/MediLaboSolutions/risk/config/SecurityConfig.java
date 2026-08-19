package com.openclassrooms.MediLaboSolutions.risk.config;

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
 * Configuration de la sécurité du service de gestion des risques.
 * <p>
 * Cette classe configure l'authentification HTTP Basic et définit les règles
 * d'accès aux différents endpoints de l'application.
 */
@Configuration
public class SecurityConfig {

    /** Nom d'utilisateur utilisé pour l'authentification, défini dans la configuration. */
    @Value("${security.user.name}")
    private String username;

    /** Mot de passe utilisé pour l'authentification, défini dans la configuration. */
    @Value("${security.user.password}")
    private String password;

    /**
     * Crée l'encodeur utilisé pour sécuriser les mots de passe.
     *
     * @return un encodeur basé sur l'algorithme BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure l'utilisateur utilisé pour l'authentification.
     * <p>
     * L'utilisateur est stocké en mémoire et possède le rôle {@code GATEWAY}.
     * Le mot de passe est encodé avec BCrypt avant d'être utilisé par Spring Security.
     *
     * @param encoder encodeur utilisé pour sécuriser le mot de passe
     * @return le service contenant les informations de l'utilisateur
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
     * Configure la chaîne de filtres de sécurité de l'application.
     * <p>
     * La protection CSRF est désactivée car le service utilise une API REST.
     * Les endpoints {@code /assess/**} nécessitent une authentification,
     * tandis que les autres endpoints restent accessibles sans authentification.
     * L'authentification HTTP Basic est utilisée pour sécuriser les requêtes protégées.
     *
     * @param http objet permettant de configurer la sécurité HTTP
     * @return la chaîne de filtres de sécurité configurée
     * @throws Exception si une erreur survient lors de la configuration de la sécurité
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/assess/**").authenticated()
                        .anyRequest().permitAll())
                .httpBasic(basic -> {});
        return http.build();
    }
}