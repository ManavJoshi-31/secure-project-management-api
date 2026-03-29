package com.jt.project.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Define authorization rules
                .authorizeHttpRequests(auth -> auth


                        // only ADMIN can see all users or delete a user
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        // only ADMIN and MANAGER can create or delete projects
                        .requestMatchers(HttpMethod.POST, "/api/projects").hasAnyRole("ADMIN", "MANAGER","EMPLOYEE")
                        .requestMatchers(HttpMethod.PUT, "/api/projects/**").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/projects/**").hasAnyRole("ADMIN", "MANAGER")
                        // anyone logged in can read projects
                        .requestMatchers(HttpMethod.GET, "/api/projects/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")

                        // only ADMIN and MANAGER can create or delete tasks
                        .requestMatchers(HttpMethod.POST, "/api/tasks").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers(HttpMethod.DELETE, "/api/tasks/**").hasAnyRole("ADMIN", "MANAGER")
                        // everyone can read tasks
                        .requestMatchers(HttpMethod.GET, "/api/tasks/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")
                        // everyone can update tasks (employee restriction handled in service)
                        .requestMatchers(HttpMethod.PUT, "/api/tasks/**").hasAnyRole("ADMIN", "MANAGER", "EMPLOYEE")

                        // any other request must be authenticated
                        .anyRequest().authenticated()
                )

                // Use HTTP Basic Authentication
                // Client sends Base64(username:password) in every request header
                .httpBasic(httpBasic -> httpBasic.realmName("Task Management API"))

                // Connect our custom UserDetailsService
                .authenticationProvider(authenticationProvider());

        return http.build();
    }
    // Connects UserDetailsService + PasswordEncoder together
    // This is what Spring uses to authenticate
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
    // Needed if you ever want to authenticate programmatically
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}