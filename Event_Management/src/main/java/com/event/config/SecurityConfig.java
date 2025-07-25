package com.event.config;

import com.event.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@CrossOrigin(origins = "http://localhost:4200")

public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Value("${admin.email}")
    private String adminEmail;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors(Customizer.withDefaults()).csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/users/register", "/api/users/login",
                                "/api/admin/login", "/api/users/update/**"
                        ).permitAll()

                        .requestMatchers(HttpMethod.PUT, "api/users/update/{id}").authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/feedback/**").authenticated()

                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/events",
                                "/api/events/{id}",
                                "/api/events/date",
                                "/api/events/location",
                                "/api/events/category",
                                "/api/users/{email}","/api/feedback/event-rating/**",
                                "/api/feedback/event/**"


                        ).permitAll()

                        .requestMatchers( "/api/events/event","/api/events/event/{id}","/api/users/all").access((authContext, context) -> {
                            String email = authContext.get().getName();
                            return new AuthorizationDecision(email.equals(adminEmail));
                        })
                        .requestMatchers("/api/tickets/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/feedback").permitAll()

                        // Only admin can POST notifications
                        .requestMatchers(HttpMethod.POST, "/api/notifications/**").access((authContext, context) -> {
                            String email = authContext.get().getName();
                            return new AuthorizationDecision(email.equals(adminEmail));
                        })

                        // Only admin can GET feedback for events
                        .requestMatchers(HttpMethod.GET,  "api/feedback/user/{id}")
                        .access((authContext, context) -> {
                            String email = authContext.get().getName();
                            return new AuthorizationDecision(email.equals(adminEmail));
                        })

                        .requestMatchers(HttpMethod.GET, "/api/feedback/user/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/notifications/alerts/**").authenticated()

                        // Any other request must be authenticated
                        .anyRequest().authenticated()
                )

                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH","DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
