package com.lancydive.fleetflow.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lancydive.fleetflow.dto.LoginRequest;
import com.lancydive.fleetflow.dto.LoginResponse;
import com.lancydive.fleetflow.dto.RegisterRequest;
import com.lancydive.fleetflow.dto.RegisterResponse;
import com.lancydive.fleetflow.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
		RegisterResponse response = authService.register(request);
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(response);
	}
	
	@PostMapping("/login")
	public LoginResponse login (@Valid @RequestBody LoginRequest request) {
		
		return authService.login(request);
	}
	
}
