package com.lancydive.fleetflow.exception;

public final class DriverNotFoundException extends RuntimeException{
	public DriverNotFoundException (String message) {
		super(message);
	}
}
