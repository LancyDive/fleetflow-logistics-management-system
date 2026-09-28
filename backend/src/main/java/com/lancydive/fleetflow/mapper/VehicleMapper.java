package com.lancydive.fleetflow.mapper;

import com.lancydive.fleetflow.dto.CreateVehicleRequest;
import com.lancydive.fleetflow.dto.VehicleResponse;
import com.lancydive.fleetflow.entity.User;
import com.lancydive.fleetflow.entity.Vehicle;

public final class VehicleMapper {
	private VehicleMapper() {
		
	}
	public static Vehicle toVehicle ( CreateVehicleRequest request) {
		return Vehicle.builder()
				.registrationNumber(request.getRegistrationNumber())
				.model(request.getModel())
				.capacityKg(request.getCapacityKg())
				.brand(request.getBrand())
				.manufacturingYear(request.getManufacturingYear())
				.fuelType(request.getFuelType())
				.chassisNumber(request.getChassisNumber())
				.vehicleType(request.getVehicleType())
				.build();
	}
	
	public static VehicleResponse toResponse(Vehicle vehicle) {
		User driver = vehicle.getAssignedDriver();
		return VehicleResponse.builder()
				.id(vehicle.getId())
				.registrationNumber(vehicle.getRegistrationNumber())
				.chassisNumber(vehicle.getChassisNumber())
				.brand(vehicle.getBrand())
				.model(vehicle.getModel())
				.manufacturingYear(vehicle.getManufacturingYear())
				.capacityKg(vehicle.getCapacityKg())
				.assignedDriverId(driver != null 
										? driver.getId()
												: null)
				.assignedDriverName(driver != null
										? driver.getFirstName()+" "+ driver.getLastName()
												: null)
				.vehicleType(vehicle.getVehicleType())
				.fuelType(vehicle.getFuelType())
				.status(vehicle.getStatus())
				.active(vehicle.getActive())
				.createdAt(vehicle.getCreatedAt())
				.updatedAt(vehicle.getUpdatedAt())
				.build();
	}
}
