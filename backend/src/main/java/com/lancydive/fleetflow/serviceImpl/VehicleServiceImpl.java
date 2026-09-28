package com.lancydive.fleetflow.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lancydive.fleetflow.constants.RoleType;
import com.lancydive.fleetflow.constants.VehicleStatus;
import com.lancydive.fleetflow.dto.AssignDriverRequest;
import com.lancydive.fleetflow.dto.CreateVehicleRequest;
import com.lancydive.fleetflow.dto.UpdateVehicleRequest;
import com.lancydive.fleetflow.dto.UpdateVehicleStatusRequest;
import com.lancydive.fleetflow.dto.VehicleResponse;
import com.lancydive.fleetflow.entity.User;
import com.lancydive.fleetflow.entity.Vehicle;
import com.lancydive.fleetflow.exception.DriverAlreadyAssignedException;
import com.lancydive.fleetflow.exception.DriverNotFoundException;
import com.lancydive.fleetflow.exception.DuplicateChassisNumberException;
import com.lancydive.fleetflow.exception.DuplicateRegistrationNumberException;
import com.lancydive.fleetflow.exception.InvalidDriverException;
import com.lancydive.fleetflow.exception.InvalidVehicleStatusException;
import com.lancydive.fleetflow.exception.InvalidVehicleUpdateException;
import com.lancydive.fleetflow.exception.VehicleNotFoundException;
import com.lancydive.fleetflow.mapper.VehicleMapper;
import com.lancydive.fleetflow.repository.ShipmentRepository;
import com.lancydive.fleetflow.repository.UserRepository;
import com.lancydive.fleetflow.repository.VehicleRepository;
import com.lancydive.fleetflow.service.VehicleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

	private final VehicleRepository vehicleRepository;
	private final UserRepository userRepository;
	private final ShipmentRepository shipmentRepository;

	@Transactional
	@Override
	public VehicleResponse createVehicle(CreateVehicleRequest request) {

		if (vehicleRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {

			throw new DuplicateRegistrationNumberException(
					"Vehicle with registration number: " + request.getRegistrationNumber() + " already exists.");
		}

		if (vehicleRepository.existsByChassisNumber(request.getChassisNumber())) {

			throw new DuplicateChassisNumberException("Vehicle with chassis number already exists.");
		}

		User driver = null;

		Long assignedDriverId = request.getAssignedDriverId();

		if (assignedDriverId != null) {

			driver = userRepository.findById(assignedDriverId).orElseThrow(
					() -> new DriverNotFoundException("Driver with ID: " + assignedDriverId + " not found."));
		}

		if (driver != null && driver.getRole() != RoleType.DRIVER) {

			throw new InvalidDriverException("Assigned user is not a driver.");
		}

		if (driver != null) {

			Optional<Vehicle> vehicleAssignedToDriver = vehicleRepository.findByAssignedDriver(driver);

			if (vehicleAssignedToDriver.isPresent()) {

				throw new DriverAlreadyAssignedException("Driver is already assigned to another vehicle.");
			}
		}

		Vehicle newVehicle = VehicleMapper.toVehicle(request);

		newVehicle.setStatus(VehicleStatus.AVAILABLE);
		newVehicle.setActive(true);
		newVehicle.setAssignedDriver(driver);

		Vehicle savedVehicle = vehicleRepository.save(newVehicle);

		return VehicleMapper.toResponse(savedVehicle);
	}

	@Transactional(readOnly = true)
	@Override
	public List<VehicleResponse> getAllVehicles() {

		return vehicleRepository.findAll().stream().map(VehicleMapper::toResponse).toList();
	}

	@Transactional(readOnly = true)
	@Override
	public VehicleResponse getVehicleByID(Long id) {

		Vehicle vehicle = vehicleRepository.findById(id)
				.orElseThrow(() -> new VehicleNotFoundException("Vehicle with ID: " + id + " not found."));

		return VehicleMapper.toResponse(vehicle);
	}

	@Transactional
	@Override
	public VehicleResponse updateVehicle(Long id, UpdateVehicleRequest request) {

		Vehicle vehicle = vehicleRepository.findById(id)
				.orElseThrow(() -> new VehicleNotFoundException("Vehicle with ID: " + id + " not found."));

		if (request.getRegistrationNumber() == null
		        && request.getChassisNumber() == null
		        && request.getBrand() == null
		        && request.getModel() == null
		        && request.getManufacturingYear() == null
		        && request.getCapacityKg() == null
		        && request.getFuelType() == null
		        && request.getVehicleType() == null) {

		    throw new InvalidVehicleUpdateException(
		            "No vehicle fields were provided for update.");
		}
		
		if (request.getRegistrationNumber() != null) {

			if (vehicleRepository.existsByRegistrationNumberAndIdNot(request.getRegistrationNumber(), id)) {

				throw new DuplicateRegistrationNumberException(
						"Vehicle with registration number: " + request.getRegistrationNumber() + " already exists.");
			}

			vehicle.setRegistrationNumber(request.getRegistrationNumber());
		}

		if (request.getChassisNumber() != null) {

			if (vehicleRepository.existsByChassisNumberAndIdNot(request.getChassisNumber(), id)) {

				throw new DuplicateChassisNumberException("Vehicle with chassis number already exists.");
			}

			vehicle.setChassisNumber(request.getChassisNumber());
		}

		if (request.getBrand() != null) {
			vehicle.setBrand(request.getBrand());
		}

		if (request.getModel() != null) {
			vehicle.setModel(request.getModel());
		}

		if (request.getManufacturingYear() != null) {
			vehicle.setManufacturingYear(request.getManufacturingYear());
		}

		if (request.getCapacityKg() != null) {
			vehicle.setCapacityKg(request.getCapacityKg());
		}

		if (request.getFuelType() != null) {
			vehicle.setFuelType(request.getFuelType());
		}

		if (request.getVehicleType() != null) {
			vehicle.setVehicleType(request.getVehicleType());
		}

		Vehicle updatedVehicle = vehicleRepository.save(vehicle);

		return VehicleMapper.toResponse(updatedVehicle);
	}

	@Transactional
	@Override
	public VehicleResponse assignDriver(Long id, AssignDriverRequest request) {

		Vehicle vehicle = vehicleRepository.findById(id)
				.orElseThrow(() -> new VehicleNotFoundException("Vehicle with ID: " + id + " not found."));

		if (!vehicle.getActive()) {

			throw new InvalidDriverException("Cannot assign a driver to an inactive vehicle.");
		}

		/*
		 * A vehicle involved in an active shipment should not have its driver changed.
		 */
		if (shipmentRepository.existsByVehicle_IdAndActiveTrue(id)) {

			throw new InvalidDriverException("Cannot change the driver while the vehicle " + "has an active shipment.");
		}

		User driver = userRepository.findById(request.getDriverId()).orElseThrow(
				() -> new DriverNotFoundException("Driver with ID: " + request.getDriverId() + " not found."));

		if (driver.getRole() != RoleType.DRIVER) {

			throw new InvalidDriverException("Assigned user is not a driver.");
		}

		Optional<Vehicle> vehicleAssignedToDriver = vehicleRepository.findByAssignedDriver(driver);

		if (vehicleAssignedToDriver.isPresent() && !vehicleAssignedToDriver.get().getId().equals(vehicle.getId())) {

			throw new DriverAlreadyAssignedException("Driver is already assigned to another vehicle.");
		}

		vehicle.setAssignedDriver(driver);

		Vehicle updatedVehicle = vehicleRepository.save(vehicle);

		return VehicleMapper.toResponse(updatedVehicle);
	}

	@Transactional
	@Override
	public VehicleResponse updateVehicleStatus(Long id, UpdateVehicleStatusRequest request) {

		Vehicle vehicle = vehicleRepository.findById(id)
				.orElseThrow(() -> new VehicleNotFoundException("Vehicle with ID: " + id + " not found."));

		VehicleStatus requestedStatus = request.getStatus();

		/*
		 * IN_USE is controlled by ShipmentService.
		 */
		if (requestedStatus == VehicleStatus.IN_USE) {

			throw new InvalidVehicleStatusException("IN_USE status is controlled by shipment operations.");
		}

		/*
		 * If the vehicle has an active shipment, its operational state must not be
		 * changed manually.
		 */
		if (vehicle.getStatus() == VehicleStatus.IN_USE && shipmentRepository.existsByVehicle_IdAndActiveTrue(id)) {

			throw new InvalidVehicleStatusException("Cannot change vehicle status while it has an active shipment.");
		}

		vehicle.setStatus(requestedStatus);

		Vehicle updatedVehicle = vehicleRepository.save(vehicle);

		return VehicleMapper.toResponse(updatedVehicle);
	}

	@Transactional
	@Override
	public void deleteVehicleById(Long id) {

		Vehicle vehicle = vehicleRepository.findById(id)
				.orElseThrow(() -> new VehicleNotFoundException("Vehicle with ID: " + id + " not found."));

		vehicle.setActive(false);

		vehicleRepository.save(vehicle);
	}
}