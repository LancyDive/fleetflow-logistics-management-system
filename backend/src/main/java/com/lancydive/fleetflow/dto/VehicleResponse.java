package com.lancydive.fleetflow.dto;

import java.time.LocalDateTime;

import com.lancydive.fleetflow.constants.FuelType;
import com.lancydive.fleetflow.constants.VehicleStatus;
import com.lancydive.fleetflow.constants.VehicleType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponse {
	
	private Long id;

	private String registrationNumber;
	private String chassisNumber;

	private String brand;
	private String model;
	private VehicleType vehicleType;
	private FuelType fuelType;

	private Integer manufacturingYear;
	private Double capacityKg;

	private VehicleStatus status;
	private Boolean active;

	private Long assignedDriverId;
	private String assignedDriverName;

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
}
