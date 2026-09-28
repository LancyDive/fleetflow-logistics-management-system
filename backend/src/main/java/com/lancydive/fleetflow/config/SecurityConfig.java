package com.lancydive.fleetflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lancydive.fleetflow.security.CustomUserDetailsService;
import com.lancydive.fleetflow.security.JwtAuthenticationFilter;
import com.lancydive.fleetflow.security.JwtService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomUserDetailsService customUserDetailsService;
	private final JwtService jwtService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						// =========================
						// AUTHENTICATION
						// =========================

						.requestMatchers("/api/auth/**").permitAll()

						// =========================
						// VEHICLES
						// =========================

						// View vehicles
						.requestMatchers(HttpMethod.GET, "/api/vehicles/**").hasAnyRole("ADMIN", "MANAGER", "DRIVER")

						// Create vehicle
						.requestMatchers(HttpMethod.POST, "/api/vehicles").hasAnyRole("ADMIN", "MANAGER")

						// Update vehicle profile
						.requestMatchers(HttpMethod.PATCH, "/api/vehicles/*").hasAnyRole("ADMIN", "MANAGER")

						// Assign / change driver
						.requestMatchers(HttpMethod.PUT, "/api/vehicles/*/driver").hasAnyRole("ADMIN", "MANAGER")

						// Change vehicle status
						.requestMatchers(HttpMethod.PUT, "/api/vehicles/*/status").hasAnyRole("ADMIN", "MANAGER")

						// Delete vehicle
						.requestMatchers(HttpMethod.DELETE, "/api/vehicles/*").hasRole("ADMIN")

						// =========================
						// SHIPMENTS
						// =========================

						// Create shipment
						.requestMatchers(HttpMethod.POST, "/api/shipments/**").hasAnyRole("ADMIN", "MANAGER")

						// Assign shipment
						.requestMatchers(HttpMethod.PUT, "/api/shipments/*/assign").hasAnyRole("ADMIN", "MANAGER")

						// Pickup shipment
						.requestMatchers(HttpMethod.PUT, "/api/shipments/*/pickup").hasRole("DRIVER")

						// Start transit
						.requestMatchers(HttpMethod.PUT, "/api/shipments/*/transit").hasRole("DRIVER")

						// Deliver shipment
						.requestMatchers(HttpMethod.PUT, "/api/shipments/*/deliver").hasRole("DRIVER")

						// Update shipment
						.requestMatchers(HttpMethod.PUT, "/api/shipments/*").hasAnyRole("ADMIN", "MANAGER")

						// Delete shipment
						.requestMatchers(HttpMethod.DELETE, "/api/shipments/*").hasAnyRole("ADMIN", "MANAGER")

						// View shipments
						.requestMatchers(HttpMethod.GET, "/api/shipments/**").hasAnyRole("ADMIN", "MANAGER", "DRIVER")

						// =========================
						// USERS
						// =========================

						// View all users
						.requestMatchers(HttpMethod.GET, "/api/users").hasAnyRole("ADMIN", "MANAGER")

						// View a specific user
						.requestMatchers(HttpMethod.GET, "/api/users/*").hasAnyRole("ADMIN", "MANAGER")

						// Update user profile
						.requestMatchers(HttpMethod.PATCH, "/api/users/*").hasAnyRole("ADMIN", "MANAGER")

						// Change user role
						.requestMatchers(HttpMethod.PUT, "/api/users/*/role").hasRole("ADMIN")

						// Enable / disable user
						.requestMatchers(HttpMethod.PUT, "/api/users/*/status").hasRole("ADMIN")

						// =========================
						// EVERYTHING ELSE
						// =========================

						.anyRequest().authenticated()// For every other request, the user must be authenticated."
				).authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
		// provider.setUserDetailsService(customUserDetailsService);
		provider.setPasswordEncoder(passwordEncoder());

		return provider;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(jwtService, customUserDetailsService);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

}
