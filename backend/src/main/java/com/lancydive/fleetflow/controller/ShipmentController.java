package com.lancydive.fleetflow.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lancydive.fleetflow.dto.AssignShipmentRequest;
import com.lancydive.fleetflow.dto.CreateShipmentRequest;
import com.lancydive.fleetflow.dto.ShipmentResponse;
import com.lancydive.fleetflow.dto.UpdateShipmentRequest;
import com.lancydive.fleetflow.service.ShipmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentService shipmentService;

    @PostMapping
    public ResponseEntity<ShipmentResponse> createShipment(
            @Valid @RequestBody CreateShipmentRequest request) {

        ShipmentResponse response = shipmentService.createShipment(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ShipmentResponse>> getAllShipments() {

        List<ShipmentResponse> shipments = shipmentService.getAllShipments();

        return ResponseEntity.ok(shipments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentResponse> getShipmentById(
            @PathVariable Long id) {

        ShipmentResponse response = shipmentService.getShipmentById(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShipmentResponse> updateShipment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateShipmentRequest request) {

        ShipmentResponse response =
                shipmentService.updateShipment(id, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShipmentById(
            @PathVariable Long id) {

        shipmentService.deleteShipmentById(id);

        return ResponseEntity.ok(
                "Shipment with Id: " + id + " deleted");
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<ShipmentResponse> assignShipment(
            @PathVariable Long id,
            @Valid @RequestBody AssignShipmentRequest request) {

        ShipmentResponse response =
                shipmentService.assignShipment(id, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/pickup")
    public ResponseEntity<ShipmentResponse> markAsPickedUp(
            @PathVariable Long id) {

        ShipmentResponse response =
                shipmentService.markAsPickedUp(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/transit")
    public ResponseEntity<ShipmentResponse> startTransit(
            @PathVariable Long id) {

        ShipmentResponse response =
                shipmentService.startTransit(id);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deliver")
    public ResponseEntity<ShipmentResponse> markAsDelivered(
            @PathVariable Long id) {

        ShipmentResponse response =
                shipmentService.markAsDelivered(id);

        return ResponseEntity.ok(response);
    }
    
}