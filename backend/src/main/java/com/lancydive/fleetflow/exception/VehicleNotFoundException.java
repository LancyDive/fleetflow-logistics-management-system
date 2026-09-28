package com.lancydive.fleetflow.exception;

public final class VehicleNotFoundException extends RuntimeException {
	public VehicleNotFoundException ( String message) {
		super(message);
	}
}
