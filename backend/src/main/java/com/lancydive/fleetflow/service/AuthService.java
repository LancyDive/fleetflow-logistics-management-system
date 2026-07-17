package com.lancydive.fleetflow.service;

import com.lancydive.fleetflow.dto.LoginRequest;
import com.lancydive.fleetflow.dto.LoginResponse;
import com.lancydive.fleetflow.dto.RegisterRequest;
import com.lancydive.fleetflow.dto.RegisterResponse;

public interface AuthService {
	RegisterResponse register (RegisterRequest request);
	 public LoginResponse login (LoginRequest request);
}
