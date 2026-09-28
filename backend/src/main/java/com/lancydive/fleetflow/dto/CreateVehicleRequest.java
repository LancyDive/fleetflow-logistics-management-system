package com.lancydive.fleetflow.dto;

import com.lancydive.fleetflow.constants.FuelType;
import com.lancydive.fleetflow.constants.VehicleType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
public class CreateVehicleRequest {
	@NotBlank
	private String registrationNumber;
	
	@NotBlank
	private String model;
	
	@Positive
	@NotNull
	private Double capacityKg;
	
	@NotBlank
	private String brand;
	
	@NotNull 
	@Min(1980)
	@Max(2100)
	private Integer manufacturingYear;
	
	@NotNull
	private FuelType fuelType;
	
	@NotBlank
	private String chassisNumber;
	
	@NotNull
	private VehicleType vehicleType;
	
	private Long assignedDriverId;
}
