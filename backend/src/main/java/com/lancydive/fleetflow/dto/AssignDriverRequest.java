package com.lancydive.fleetflow.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignDriverRequest {

    @NotNull(message = "Driver ID is required")
    private Long driverId;
}