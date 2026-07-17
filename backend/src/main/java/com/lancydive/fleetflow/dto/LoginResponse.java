package com.lancydive.fleetflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
	private String token;
	private String tokenType;
	private String role;
	private String firstName;
}
