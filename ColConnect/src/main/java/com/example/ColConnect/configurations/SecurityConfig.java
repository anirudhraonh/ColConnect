package com.example.ColConnect.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//replaced old /login endpoint with this
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login", "/css/**", "/js/**").permitAll() // Public paths
                        .anyRequest().authenticated()                    // Everything else is locked
                )
                .formLogin(form -> form
                        .loginPage("/login")               // Your custom login view
                        .defaultSuccessUrl("/homepage")    // Where to go after success
                        .permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)               // Optional: prevent double logins
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // The URL that triggers logout
                        .logoutSuccessUrl("/login?logout") // Where to go after success
                        .invalidateHttpSession(true) // Kill the session
                        .deleteCookies("JSESSIONID") // Wipe the session cookie
                        .permitAll()
                );

        return http.build();
    }

}
