package com.lancydive.fleetflow.exception;

public final class UserNotFoundException extends RuntimeException {
	public UserNotFoundException (String message) {
		super(message);
	}
}
