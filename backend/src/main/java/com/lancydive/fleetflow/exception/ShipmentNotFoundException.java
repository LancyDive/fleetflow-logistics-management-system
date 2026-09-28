package com.lancydive.fleetflow.exception;

public final class ShipmentNotFoundException extends RuntimeException {
	public ShipmentNotFoundException (String message) {
		super(message);
	}
}
