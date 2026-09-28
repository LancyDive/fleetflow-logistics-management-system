package com.lancydive.fleetflow.mapper;

import com.lancydive.fleetflow.dto.CreateShipmentRequest;
import com.lancydive.fleetflow.dto.ShipmentResponse;
import com.lancydive.fleetflow.entity.Shipment;
import com.lancydive.fleetflow.entity.User;
import com.lancydive.fleetflow.entity.Vehicle;


public class ShipmentMapper {
	public static Shipment  toShipment (CreateShipmentRequest request) {
		return  Shipment.builder()
				.pickupAddress(request.getPickupAddress())
				.deliveryAddress(request.getDeliveryAddress())
				.weightKg(request.getWeightKg())
				.build();				
	}
	
	public static ShipmentResponse  toResponse (Shipment shipment) {
		User driver = shipment.getDriver();
		Vehicle vehicle = shipment.getVehicle();
		return ShipmentResponse.builder()
				.id(shipment.getId())
				.trackingNumber(shipment.getTrackingNumber())
				.pickupAddress(shipment.getPickupAddress())
				.deliveryAddress(shipment.getDeliveryAddress())
				.weightKg(shipment.getWeightKg())
				.status(shipment.getStatus())
				.vehicleId(vehicle != null ?
						vehicle.getId()
						: null)
				.vehicleRegistrationNumber(vehicle != null ?
						vehicle.getRegistrationNumber()
						: null)
				.driverId(driver !=null ?
						driver.getId()
						: null)
				.driverName(driver != null ?
						driver.getFirstName()+" "+driver.getLastName()
						: null)
				.active(shipment.getActive())
				.createdAt(shipment.getCreatedAt())
				.updatedAt(shipment.getUpdatedAt())
				.build();
				
				
	}
}
