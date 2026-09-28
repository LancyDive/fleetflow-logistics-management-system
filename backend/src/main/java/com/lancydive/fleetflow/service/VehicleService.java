package com.lancydive.fleetflow.service;

import java.util.List;

import com.lancydive.fleetflow.dto.AssignDriverRequest;
import com.lancydive.fleetflow.dto.CreateVehicleRequest;
import com.lancydive.fleetflow.dto.UpdateVehicleRequest;
import com.lancydive.fleetflow.dto.UpdateVehicleStatusRequest;
import com.lancydive.fleetflow.dto.VehicleResponse;

public interface VehicleService {

    VehicleResponse createVehicle(CreateVehicleRequest request);

    List<VehicleResponse> getAllVehicles();

    VehicleResponse getVehicleByID(Long id);

    VehicleResponse updateVehicle(Long id, UpdateVehicleRequest request);

    VehicleResponse assignDriver(Long id, AssignDriverRequest request);

    VehicleResponse updateVehicleStatus(Long id, UpdateVehicleStatusRequest request);

    void deleteVehicleById(Long id);
}