package com.lancydive.fleetflow.controller;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
		
		
		return authService.register(request);
	}
	
}
