package com.lancydive.fleetflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lancydive.fleetflow.entity.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
	 boolean existsByVehicle_IdAndActiveTrue(Long vehicleId);
}
