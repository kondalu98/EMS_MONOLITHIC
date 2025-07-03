package com.event.config;

import com.event.security.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Value("${admin.email}")
    private String adminEmail;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // Public endpoints
                .requestMatchers(
                        "/api/users/**",
                        "/api/admin/login"
                ).permitAll()

                    .requestMatchers(HttpMethod.PUT, "api/users/update/{id}").authenticated()
                //  feedback POST
                .requestMatchers(HttpMethod.POST, "/api/feedback/**").authenticated()

                // Public event GET endpoints
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/events",
                        "/api/events/{id}",
                        "/api/events/date",
                        "/api/events/location",
                        "/api/events/category"
                ).permitAll()

                // Only admin can POST/PUT/DELETE events
                .requestMatchers("/api/events/event","/api/events/event/{id}").access((authContext, context) -> {
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
                .requestMatchers(HttpMethod.GET, "/api/feedback/event/**", "/api/feedback/event-rating/**","api/feedback/user/{id}")
                        .access((authContext, context) -> {
                            String email = authContext.get().getName();
                            return new AuthorizationDecision(email.equals(adminEmail));
                        })

                // Feedback GET by user is public
                .requestMatchers(HttpMethod.GET, "/api/feedback/user/**").permitAll()


                .requestMatchers(HttpMethod.GET, "/api/notifications/alerts/**").authenticated()

                // Any other request must be authenticated
                .anyRequest().authenticated()
            )

            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
