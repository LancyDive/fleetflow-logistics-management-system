package com.lancydive.fleetflow.service;

import java.util.List;

import com.lancydive.fleetflow.dto.AssignShipmentRequest;
import com.lancydive.fleetflow.dto.CreateShipmentRequest;
import com.lancydive.fleetflow.dto.ShipmentResponse;
import com.lancydive.fleetflow.dto.UpdateShipmentRequest;

public interface ShipmentService {
	ShipmentResponse createShipment(CreateShipmentRequest request);

    List<ShipmentResponse> getAllShipments();

    ShipmentResponse getShipmentById(Long id);

    ShipmentResponse updateShipment(Long id, UpdateShipmentRequest request);

    void deleteShipmentById(Long id);

    ShipmentResponse assignShipment(Long id, AssignShipmentRequest request);
    
    ShipmentResponse markAsPickedUp(Long id);

    ShipmentResponse startTransit(Long id);

    ShipmentResponse markAsDelivered(Long id);
}
