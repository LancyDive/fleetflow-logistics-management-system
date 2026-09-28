package com.lancydive.fleetflow.mapper;

import com.lancydive.fleetflow.dto.RegisterRequest;
import com.lancydive.fleetflow.dto.UserResponse;
import com.lancydive.fleetflow.entity.User;


public final class UserMapper {
	private UserMapper() {
		
	}
	public static User toUser ( RegisterRequest request) {
		return User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.password(request.getPassword())
				.build();
	}
	
    public static UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .enabled(user.isEnabled())
                .assignedVehicleId(
                        user.getAssignedVehicle() != null
                                ? user.getAssignedVehicle().getId()
                                : null
                )
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
