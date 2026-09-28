package com.lancydive.fleetflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import java.util.stream.Collectors;
import org.springframework.web.bind.MethodArgumentNotValidException;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExists(
            EmailAlreadyExistsException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Registration failed: " + e.getMessage());
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatch(
            PasswordMismatchException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Registration failed: " + e.getMessage());
    }

    @ExceptionHandler(DuplicateRegistrationNumberException.class)
    public ResponseEntity<String> handleDuplicateRegistrationNumber(
            DuplicateRegistrationNumberException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Vehicle creation failed: " + e.getMessage());
    }

    @ExceptionHandler(DuplicateChassisNumberException.class)
    public ResponseEntity<String> handleDuplicateChassisNumber(
            DuplicateChassisNumberException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Vehicle creation failed: " + e.getMessage());
    }

    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<String> handleDriverNotFound(
            DriverNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Driver not found: " + e.getMessage());
    }

    @ExceptionHandler(InvalidDriverException.class)
    public ResponseEntity<String> handleInvalidDriver(
            InvalidDriverException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Invalid driver: " + e.getMessage());
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<String> vehicleNotFound(
            VehicleNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Vehicle not found: " + e.getMessage());
    }

    @ExceptionHandler(DriverAlreadyAssignedException.class)
    public ResponseEntity<String> driverAlreadyAssigned(
            DriverAlreadyAssignedException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Driver already assigned: " + e.getMessage());
    }

    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<String> shipmentNotFound(
            ShipmentNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Shipment not found: " + e.getMessage());
    }

    @ExceptionHandler(VehicleNotAvailableException.class)
    public ResponseEntity<String> vehicleNotAvailable(
            VehicleNotAvailableException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Vehicle not available: " + e.getMessage());
    }

    @ExceptionHandler(VehicleCapacityExceededException.class)
    public ResponseEntity<String> vehicleCapacityExceeded(
            VehicleCapacityExceededException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Vehicle capacity exceeded: " + e.getMessage());
    }

    @ExceptionHandler(InvalidShipmentStatusException.class)
    public ResponseEntity<String> invalidShipmentStatus(
            InvalidShipmentStatusException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Invalid shipment status: " + e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> userNotFound(
            UserNotFoundException e) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("User not found: " + e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDenied(
            AccessDeniedException e) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Access denied: " + e.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(
            MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Validation failed: " + message);
    }
    
    @ExceptionHandler(InvalidUserUpdateException.class)
    public ResponseEntity<String> invalidUserUpdate (InvalidUserUpdateException e){
    	return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid user update: " + e.getMessage());
    }
    
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimisticLockingFailure(
            ObjectOptimisticLockingFailureException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Update conflict: The resource was modified by another request. Please refresh and try again.");
    }
    
    @ExceptionHandler(InvalidVehicleStatusException.class)
    public ResponseEntity<String> handleInvalidVehicleStatus(
            InvalidVehicleStatusException e) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
    
    @ExceptionHandler(InvalidVehicleUpdateException.class)
    public ResponseEntity<String> handleInvalidVehicleUpdate(
            InvalidVehicleUpdateException e) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}