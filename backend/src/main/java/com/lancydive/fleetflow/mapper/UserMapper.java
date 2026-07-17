package com.lancydive.fleetflow.mapper;

import com.lancydive.fleetflow.dto.RegisterRequest;
import com.lancydive.fleetflow.entity.User;


public class UserMapper {
	public static User toUser ( RegisterRequest request) {
		return User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.password(request.getPassword())
				.build();
	}
}
