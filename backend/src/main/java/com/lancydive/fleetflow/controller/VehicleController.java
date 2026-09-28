package com.lancydive.fleetflow.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lancydive.fleetflow.dto.AssignDriverRequest;
import com.lancydive.fleetflow.dto.CreateVehicleRequest;
import com.lancydive.fleetflow.dto.UpdateVehicleRequest;
import com.lancydive.fleetflow.dto.UpdateVehicleStatusRequest;
import com.lancydive.fleetflow.dto.VehicleResponse;
import com.lancydive.fleetflow.service.VehicleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleResponse> createVehicle(
            @Valid @RequestBody CreateVehicleRequest request) {

        VehicleResponse response =
                vehicleService.createVehicle(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponse>> getAllVehicles() {

        List<VehicleResponse> vehicles =
                vehicleService.getAllVehicles();

        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponse> getVehicleById(
            @PathVariable Long id) {

        VehicleResponse vehicle =
                vehicleService.getVehicleByID(id);

        return ResponseEntity.ok(vehicle);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VehicleResponse> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleRequest request) {

        VehicleResponse vehicle =
                vehicleService.updateVehicle(id, request);

        return ResponseEntity.ok(vehicle);
    }

    @PutMapping("/{id}/driver")
    public ResponseEntity<VehicleResponse> assignDriver(
            @PathVariable Long id,
            @Valid @RequestBody AssignDriverRequest request) {

        VehicleResponse vehicle =
                vehicleService.assignDriver(id, request);

        return ResponseEntity.ok(vehicle);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<VehicleResponse> updateVehicleStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateVehicleStatusRequest request) {

        VehicleResponse vehicle =
                vehicleService.updateVehicleStatus(id, request);

        return ResponseEntity.ok(vehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVehicleById(
            @PathVariable Long id) {

        vehicleService.deleteVehicleById(id);

        return ResponseEntity.ok(
                "Vehicle by Id: " + id + " deleted");
    }
}