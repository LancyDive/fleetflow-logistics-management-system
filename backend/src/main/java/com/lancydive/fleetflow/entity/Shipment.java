package com.lancydive.fleetflow.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.lancydive.fleetflow.constants.ShipmentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shipments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shipment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Version
	@Column(nullable = false)
	private Long version;
	
	@Column(nullable = false, unique = true)
	private String trackingNumber;
	
	@Column(nullable = false)
	private String pickupAddress;

	@Column(nullable = false)
	private String deliveryAddress;

	@Column(nullable = false)
	private Double weightKg;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ShipmentStatus status;

	@ManyToOne
	@JoinColumn(name="vehicle_id")
	private Vehicle vehicle;

	@ManyToOne
	@JoinColumn(name = "driver_id")
	private User driver;

	@Column(nullable = false)
	private Boolean active;

	@Column(updatable = false)
	@CreationTimestamp
	private LocalDateTime createdAt;

	@Column
	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
