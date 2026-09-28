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
public class AssignShipmentRequest {

    @NotNull
    private Long vehicleId;

    @NotNull
    private Long driverId;
}