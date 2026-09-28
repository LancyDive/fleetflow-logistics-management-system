package com.lancydive.fleetflow.dto;

import com.lancydive.fleetflow.constants.VehicleStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateVehicleStatusRequest {

    @NotNull(message = "Vehicle status is required")
    private VehicleStatus status;
}
