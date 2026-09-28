package com.lancydive.fleetflow.dto;

import com.lancydive.fleetflow.constants.RoleType;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRoleRequest {

    @NotNull
    private RoleType role;
}
