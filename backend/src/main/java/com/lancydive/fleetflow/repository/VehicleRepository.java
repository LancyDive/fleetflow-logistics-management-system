package com.lancydive.fleetflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lancydive.fleetflow.entity.User;
import com.lancydive.fleetflow.entity.Vehicle;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	boolean existsByRegistrationNumber(String registrationNumber);

	boolean existsByChassisNumber(String chassisNumber);

	boolean existsByRegistrationNumberAndIdNot(String registrationNumber, Long id);

	boolean existsByChassisNumberAndIdNot(String chassisNumber, Long id);

	Optional<Vehicle> findByAssignedDriver(User driver);

}
