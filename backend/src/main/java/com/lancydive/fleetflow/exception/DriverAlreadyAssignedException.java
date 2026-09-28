package com.lancydive.fleetflow.exception;

public final class DriverAlreadyAssignedException extends RuntimeException {
	public DriverAlreadyAssignedException (String message) {
		super(message);
	}
}
