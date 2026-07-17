package com.lancydive.fleetflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder () {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable());
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/auth/**")
				.permitAll()
				.anyRequest().authenticated()//For every other request, the user must be authenticated."
				);
		return http.build();
	}
}
