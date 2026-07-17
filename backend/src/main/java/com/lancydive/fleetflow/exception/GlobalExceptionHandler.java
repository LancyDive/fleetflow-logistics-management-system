package com.lancydive.fleetflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<String> handleEmailAlreadyExists (EmailAlreadyExistsException e ) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Registration failed :" +e.getMessage());
	}
	
	@ExceptionHandler(PasswordMismatchException.class)
	public ResponseEntity<String> handlePasswordMismatch (PasswordMismatchException e) {
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body("Registration failed: "+ e.getMessage());
	}
}
