package com.lancydive.fleetflow.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateShipmentRequest {
	@NotBlank
	private String pickupAddress;

	@NotBlank
	private String deliveryAddress;

	@NotNull
	@Positive
	private Double weightKg;

	
	private Boolean active;
}
