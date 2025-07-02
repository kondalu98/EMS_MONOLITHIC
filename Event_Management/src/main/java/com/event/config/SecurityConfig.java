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
		return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
				
				.requestMatchers("/api/users/**", "/api/admin/login").permitAll().
				
				 requestMatchers("/api/tickets/**").permitAll()
				 
				.requestMatchers(HttpMethod.POST, "/api/feedback/**").permitAll()
				
				.requestMatchers(HttpMethod.GET, "/api/events", "/api/events/{id}", "/api/events/date",
						"/api/events/location", "/api/events/category"
						).permitAll()
				
				//only admin can post events
				.requestMatchers("/api/events/**", "/api/events/event/**").access((authContext, context) -> {
					    String email = authContext.get().getName(); // <-- Breakpoint here
					    System.out.println("Authenticated Email: " + email); // Add this for quick checks
					    System.out.println("Admin Email from Config: " + adminEmail); // Add this
					    return new AuthorizationDecision(email.equals(adminEmail));
					})
				
				.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/v3/api-docs",
						"/v3/api-docs.yaml").permitAll()
				

				// Only admin can POST notifications
				.requestMatchers(HttpMethod.POST, "/api/notifications/**").access((authContext, context) -> {
					String email = authContext.get().getName();
					return new AuthorizationDecision(email.equals(adminEmail));
				})

						.requestMatchers(HttpMethod.POST, "/api/feedback").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/feedback/user/**").permitAll()
						.requestMatchers(HttpMethod.GET, "/api/feedback/event/**", "/api/feedback/event-rating/**")
						.access((authContext, context) -> {
							String email = authContext.get().getName();
							return new AuthorizationDecision(email.equals(adminEmail));
						})
				.requestMatchers(HttpMethod.GET, "/api/notifications/alerts/**").authenticated()
				.anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
}
