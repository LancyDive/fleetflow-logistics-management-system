package com.lancydive.fleetflow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
	@NotBlank
	@Email
	private String email;
	
	@NotBlank
	private String password;
	
}
