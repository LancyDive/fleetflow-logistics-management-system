package com.lancydive.fleetflow.dto;

import java.time.LocalDateTime;

import com.lancydive.fleetflow.constants.RoleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private RoleType role;
    private boolean enabled;
    private Long assignedVehicleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
