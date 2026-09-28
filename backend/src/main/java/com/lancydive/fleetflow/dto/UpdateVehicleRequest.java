package com.lancydive.fleetflow.dto;

import com.lancydive.fleetflow.constants.FuelType;
import com.lancydive.fleetflow.constants.VehicleType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateVehicleRequest {

	@Pattern(regexp = ".*\\S.*", message = "Registration number cannot be blank")
	private String registrationNumber;

	@Pattern(regexp = ".*\\S.*", message = "Chassis number cannot be blank")
	private String chassisNumber;

	@Pattern(regexp = ".*\\S.*", message = "Brand cannot be blank")
	private String brand;

	@Pattern(regexp = ".*\\S.*", message = "Model cannot be blank")
	private String model;

	@Min(value = 1980, message = "Manufacturing year must be 1980 or later")
	@Max(value = 2100, message = "Manufacturing year must be 2100 or earlier")
	private Integer manufacturingYear;

	@Positive(message = "Capacity must be positive")
	private Double capacityKg;

	private FuelType fuelType;

	private VehicleType vehicleType;
}