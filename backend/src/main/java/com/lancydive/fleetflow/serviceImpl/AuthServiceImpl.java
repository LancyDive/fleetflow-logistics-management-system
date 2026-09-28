package com.lancydive.fleetflow.serviceImpl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lancydive.fleetflow.constants.RoleType;
import com.lancydive.fleetflow.dto.LoginRequest;
import com.lancydive.fleetflow.dto.LoginResponse;
import com.lancydive.fleetflow.dto.RegisterRequest;
import com.lancydive.fleetflow.dto.RegisterResponse;
import com.lancydive.fleetflow.entity.User;
import com.lancydive.fleetflow.exception.EmailAlreadyExistsException;
import com.lancydive.fleetflow.exception.InvalidCredentialsException;
import com.lancydive.fleetflow.exception.PasswordMismatchException;
import com.lancydive.fleetflow.mapper.UserMapper;
import com.lancydive.fleetflow.repository.UserRepository;
import com.lancydive.fleetflow.security.JwtService;
import com.lancydive.fleetflow.service.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
 
	 @Transactional
	 @Override
     public RegisterResponse register (RegisterRequest request) {
    	if (userRepository.existsByEmail(request.getEmail())) {
    		throw new EmailAlreadyExistsException("Email Already registered");
    	}
    	if (!request.getPassword().equals(request.getConfirmPassword())) {
    		throw new PasswordMismatchException("Password and confirm password do not match.");
    	}
    	
    	 User user = UserMapper.toUser(request);
    	 user.setPassword(
    			 passwordEncoder.encode(request.getPassword()));
    	 user.setEnabled(true);
    	 user.setRole(RoleType.DRIVER);
    	 
    	 User savedUser = userRepository.save(user);
    	
    	 return RegisterResponse.builder()
    		        .message("User successfully registered")
    		        .userId(savedUser.getId())
    		        .build();
     }
	 
	 @Transactional
	 @Override
	 public LoginResponse login (LoginRequest request) {
		 try {
			    authenticationManager.authenticate(
			        new UsernamePasswordAuthenticationToken(
			            request.getEmail(),
			            request.getPassword()
			        )
			    );
			} catch (AuthenticationException  e) {
			    throw new InvalidCredentialsException("Invalid email or password.");
			}
		 User existingUser = userRepository.findByEmail(request.getEmail())
				 .orElseThrow(()-> new IllegalStateException("Authenticated user not found."));
		 String token = jwtService.generateAccessToken(existingUser);
		 
		 return LoginResponse.builder()
				 .token(token)
				 .tokenType("Bearer")
				 .role(existingUser.getRole().name())
				 .firstName(existingUser.getFirstName())
				 .build();
	 }
}
