package com.lancydive.fleetflow.serviceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.lancydive.fleetflow.constants.RoleType;
import com.lancydive.fleetflow.constants.ShipmentStatus;
import com.lancydive.fleetflow.constants.VehicleStatus;
import com.lancydive.fleetflow.dto.AssignShipmentRequest;
import com.lancydive.fleetflow.dto.CreateShipmentRequest;
import com.lancydive.fleetflow.dto.ShipmentResponse;
import com.lancydive.fleetflow.dto.UpdateShipmentRequest;
import com.lancydive.fleetflow.entity.Shipment;
import com.lancydive.fleetflow.entity.User;
import com.lancydive.fleetflow.entity.Vehicle;
import com.lancydive.fleetflow.exception.DriverNotFoundException;
import com.lancydive.fleetflow.exception.InvalidDriverException;
import com.lancydive.fleetflow.exception.InvalidShipmentStatusException;
import com.lancydive.fleetflow.exception.ShipmentNotFoundException;
import com.lancydive.fleetflow.exception.VehicleCapacityExceededException;
import com.lancydive.fleetflow.exception.VehicleNotAvailableException;
import com.lancydive.fleetflow.exception.VehicleNotFoundException;
import com.lancydive.fleetflow.mapper.ShipmentMapper;
import com.lancydive.fleetflow.repository.ShipmentRepository;
import com.lancydive.fleetflow.repository.UserRepository;
import com.lancydive.fleetflow.repository.VehicleRepository;
import com.lancydive.fleetflow.service.ShipmentService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
	
@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {
	private final ShipmentRepository shipmentRepository;
	private final VehicleRepository vehicleRepository;
	private final UserRepository userRepository;
	
	private String generateTrackingNumber () {
		String date = LocalDate.now()
				.toString()
				.replace("-", "");
		
		String uniqueId = UUID.randomUUID()
				.toString()
				.substring(0, 6)
				.toUpperCase();
		return "FLT" + date + "-"+ uniqueId;
	}

	@Transactional
	@Override
	public ShipmentResponse createShipment(CreateShipmentRequest request) {
		Shipment shipment = ShipmentMapper.toShipment(request);
		shipment.setTrackingNumber(generateTrackingNumber());
		shipment.setStatus(ShipmentStatus.CREATED);
		shipment.setActive(true);
		
		Shipment savedShipment = shipmentRepository.save(shipment);
		return ShipmentMapper.toResponse(savedShipment);
	}

	@Transactional(readOnly = true)
	@Override
	public List<ShipmentResponse> getAllShipments() {
		List<Shipment> shipments  = shipmentRepository.findAll();
		return shipments.stream()
				.map(ShipmentMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	@Override
	public ShipmentResponse getShipmentById(Long id) {
		Shipment shipment = shipmentRepository.findById(id)
				.orElseThrow(()-> new ShipmentNotFoundException("Shipment with: " + id + " not found"));
		
		return ShipmentMapper.toResponse(shipment);
	}
	
	@Transactional
	@Override
	public ShipmentResponse updateShipment(Long id, UpdateShipmentRequest request) {
		Shipment shipment = shipmentRepository.findById(id)
				.orElseThrow(()->
						new ShipmentNotFoundException(
								 "Shipment with Id: " + id + " not found"));
		
		shipment.setPickupAddress(request.getPickupAddress());
		shipment.setDeliveryAddress(request.getDeliveryAddress());
		shipment.setWeightKg(request.getWeightKg());
		
		if (request.getActive() != null) {
		    shipment.setActive(request.getActive());
		}
		
		Shipment updatedShipment = shipmentRepository.save(shipment);
		
		return ShipmentMapper.toResponse(updatedShipment);
	}

	@Transactional
	@Override
	public void deleteShipmentById(Long id) {
		 Shipment shipment = shipmentRepository.findById(id).orElseThrow(()->
		 						new ShipmentNotFoundException("Shipment with Id: " + id + " not found"));
		 shipment.setActive(false);
		 shipmentRepository.save(shipment);
	}

	@Transactional
	@Override
	public ShipmentResponse assignShipment(Long id, AssignShipmentRequest request) {
		Shipment shipment = shipmentRepository.findById(id).orElseThrow(()->
			new ShipmentNotFoundException("Shipment with Id: " + id + " not found"));
		Vehicle vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(()->
			new VehicleNotFoundException("Vehicle with Id: " + request.getVehicleId() + " not found"));
		User driver = userRepository.findById(request.getDriverId()).orElseThrow(()->
			new DriverNotFoundException("Driver with Id: " + request.getDriverId() + " not found"));
		
		
		if(driver.getRole() != RoleType.DRIVER) {
			throw new InvalidDriverException("Assigned user is not a driver.");
		}
		
		if (!shipment.getActive()) {
		    throw new InvalidShipmentStatusException(
		            "Cannot assign an inactive shipment.");
		}
		
		if (shipment.getStatus() != ShipmentStatus.CREATED) {
		    throw new InvalidShipmentStatusException(
		            "Only CREATED shipments can be assigned.");
		}
		
		if(!vehicle.getActive()) {
			throw new VehicleNotAvailableException(
	                "Vehicle with Id: " + vehicle.getId() + " is inactive.");
		}
		
		if (vehicle.getStatus() != VehicleStatus.AVAILABLE) {
		    throw new VehicleNotAvailableException(
		            "Vehicle with Id: " + vehicle.getId()
		            + " is not available for shipment assignment.");
		}
		
		if(vehicle.getCapacityKg() < shipment.getWeightKg()) {
			throw new VehicleCapacityExceededException(
		            "Shipment weight exceeds vehicle capacity.");
		}
		
		if (vehicle.getAssignedDriver() == null) {
		    throw new InvalidDriverException(
		            "Vehicle does not have a driver assigned.");
		}

		if (!vehicle.getAssignedDriver().getId().equals(driver.getId())) {
		    throw new InvalidDriverException(
		            "Driver is not assigned to this vehicle.");
		}
		
		shipment.setVehicle(vehicle);
		shipment.setDriver(driver);
		shipment.setStatus(ShipmentStatus.ASSIGNED);
		
		vehicle.setStatus(VehicleStatus.IN_USE);
		
		Shipment savedShipment = shipmentRepository.save(shipment);
		return ShipmentMapper.toResponse(savedShipment);
	}

	@Transactional
	@Override
	public ShipmentResponse markAsPickedUp(Long id) {
		Shipment shipment = shipmentRepository.findById(id)
				.orElseThrow(() ->
                new ShipmentNotFoundException(
                        "Shipment with Id: " + id + " not found"));
		 if (shipment.getStatus() != ShipmentStatus.ASSIGNED) {
		        throw new InvalidShipmentStatusException(
		                "Shipment can only be picked up when it is ASSIGNED.");
		    }
		 
		 if (!shipment.getActive()) {
			    throw new InvalidShipmentStatusException(
			            "Cannot pick up an inactive shipment.");
			}
		 
		 shipment.setStatus(ShipmentStatus.PICKED_UP);

		 Shipment updatedShipment = shipmentRepository.save(shipment);

		 return ShipmentMapper.toResponse(updatedShipment);
	}

	@Transactional
	@Override
	public ShipmentResponse startTransit(Long id) {
	    Shipment shipment = shipmentRepository.findById(id)
	            .orElseThrow(() ->
	                    new ShipmentNotFoundException(
	                            "Shipment with Id: " + id + " not found"));

	    if (shipment.getStatus() != ShipmentStatus.PICKED_UP) {
	        throw new InvalidShipmentStatusException(
	                "Shipment can only start transit when it is PICKED_UP.");
	    }

	    if (!shipment.getActive()) {
	        throw new InvalidShipmentStatusException(
	                "Cannot start transit for an inactive shipment.");
	    }

	    Vehicle vehicle = shipment.getVehicle();

	    if (vehicle == null) {
	        throw new VehicleNotFoundException(
	                "No vehicle is assigned to this shipment.");
	    }

	    if (!vehicle.getActive()) {
	        throw new VehicleNotAvailableException(
	                "Assigned vehicle is inactive.");
	    }

	    if (vehicle.getStatus() != VehicleStatus.IN_USE) {
	        throw new VehicleNotAvailableException(
	                "Assigned vehicle is not currently in use.");
	    }

	    shipment.setStatus(ShipmentStatus.IN_TRANSIT);

	    Shipment updatedShipment = shipmentRepository.save(shipment);

	    return ShipmentMapper.toResponse(updatedShipment);
	}
		

	@Transactional
	@Override
	public ShipmentResponse markAsDelivered(Long id) {

	    Shipment shipment = shipmentRepository.findById(id)
	            .orElseThrow(() ->
	                    new ShipmentNotFoundException(
	                            "Shipment with Id: " + id + " not found"));

	    if (shipment.getStatus() != ShipmentStatus.IN_TRANSIT) {
	        throw new InvalidShipmentStatusException(
	                "Shipment can only be delivered when it is IN_TRANSIT.");
	    }
	    
	    if (!shipment.getActive()) {
	        throw new InvalidShipmentStatusException(
	                "Cannot deliver an inactive shipment.");
	    }

	    Vehicle vehicle = shipment.getVehicle();

	    if (vehicle == null) {
	        throw new VehicleNotFoundException(
	                "No vehicle is assigned to this shipment.");
	    }

	    shipment.setStatus(ShipmentStatus.DELIVERED);
	    shipment.setActive(false);

	    vehicle.setStatus(VehicleStatus.AVAILABLE);
	    
	    Shipment updatedShipment = shipmentRepository.save(shipment);
	    
	    return ShipmentMapper.toResponse(updatedShipment);
	}

}
