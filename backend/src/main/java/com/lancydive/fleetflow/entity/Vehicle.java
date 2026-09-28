package com.lancydive.fleetflow.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.lancydive.fleetflow.constants.FuelType;
import com.lancydive.fleetflow.constants.VehicleStatus;
import com.lancydive.fleetflow.constants.VehicleType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Version
	@Column(nullable = false)
	private Long version;
	
	@Column(nullable = false, unique = true)
	private String registrationNumber;
	
	@Column(nullable = false)
	private String model;
	
	@Column(nullable = false)
	private Double capacityKg;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private VehicleStatus status;
	
	@OneToOne
	@JoinColumn(name = "assigned_driver_id", unique = true)
	private User assignedDriver;
	
	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@Column
	@UpdateTimestamp
	private LocalDateTime updatedAt;
	
	@Column(nullable = false)
	private String brand;
	
	@Column(nullable = false)
	private Integer manufacturingYear;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private FuelType fuelType;
	
	@Column(nullable = false, unique = true)
	private String chassisNumber;
	
	@Column(nullable = false)
	private Boolean active;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private VehicleType vehicleType;
	
}
