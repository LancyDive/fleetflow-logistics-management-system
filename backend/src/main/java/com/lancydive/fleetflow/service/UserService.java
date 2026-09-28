package com.lancydive.fleetflow.service;

import java.util.List;

import com.lancydive.fleetflow.dto.UpdateUserRequest;
import com.lancydive.fleetflow.dto.UpdateUserRoleRequest;
import com.lancydive.fleetflow.dto.UpdateUserStatusRequest;
import com.lancydive.fleetflow.dto.UserResponse;

public interface UserService {

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);
    
    UserResponse updateUser(Long id, UpdateUserRequest request);
    
    UserResponse updateUserRole( Long id, UpdateUserRoleRequest request);
    
    UserResponse updateUserStatus(Long id,UpdateUserStatusRequest request);
}
