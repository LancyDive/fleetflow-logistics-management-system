package com.lancydive.fleetflow.dto;

import java.time.LocalDateTime;

import com.lancydive.fleetflow.constants.ShipmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentResponse {
	private Long id;

	private String trackingNumber;

	private String pickupAddress;

	private String deliveryAddress;

	private Double weightKg;

	private ShipmentStatus status;

	private Long vehicleId;

	private String vehicleRegistrationNumber;

	private Long driverId;

	private String driverName;

	private Boolean active;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;
}
