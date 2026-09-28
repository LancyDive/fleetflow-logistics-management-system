package com.lancydive.fleetflow.serviceImpl;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.lancydive.fleetflow.constants.RoleType;
import com.lancydive.fleetflow.dto.UpdateUserRequest;
import com.lancydive.fleetflow.dto.UpdateUserRoleRequest;
import com.lancydive.fleetflow.dto.UpdateUserStatusRequest;
import com.lancydive.fleetflow.dto.UserResponse;
import com.lancydive.fleetflow.entity.User;
import com.lancydive.fleetflow.exception.AccessDeniedException;
import com.lancydive.fleetflow.exception.EmailAlreadyExistsException;
import com.lancydive.fleetflow.exception.InvalidUserUpdateException;
import com.lancydive.fleetflow.exception.UserNotFoundException;
import com.lancydive.fleetflow.mapper.UserMapper;
import com.lancydive.fleetflow.repository.UserRepository;
import com.lancydive.fleetflow.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with Id: " + id + " not found"));

        return UserMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(
            Long id,
            UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with Id: " + id + " not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isManager = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_MANAGER"));

        // ADMIN users cannot be modified.
        if (user.getRole() == RoleType.ADMIN) {
            throw new AccessDeniedException(
                    "ADMIN users cannot be modified.");
        }

        // MANAGER can modify DRIVER only.
        if (isManager && user.getRole() != RoleType.DRIVER) {
            throw new AccessDeniedException(
                    "MANAGER can only modify DRIVER users.");
        }
        
     // At least one field must be provided.
        boolean noFieldsProvided =
                request.getFirstName() == null
                && request.getLastName() == null
                && request.getEmail() == null;

        if (noFieldsProvided) {
            throw new InvalidUserUpdateException(
                    "At least one field must be provided for update.");
        }
        
        // Update only fields that were supplied.
        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getEmail() != null) {

            boolean emailAlreadyExists =
                    userRepository.existsByEmailAndIdNot(
                            request.getEmail(),
                            id);

            if (emailAlreadyExists) {
                throw new EmailAlreadyExistsException(
                        "Email already exists: " + request.getEmail());
            }

            user.setEmail(request.getEmail());
        }

     
        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse updateUserRole(
            Long id,
            UpdateUserRoleRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with Id: " + id + " not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        // Only ADMIN can change roles.
        if (!isAdmin) {
            throw new AccessDeniedException(
                    "Only ADMIN can change user roles.");
        }

        // ADMIN users cannot be modified.
        if (user.getRole() == RoleType.ADMIN) {
            throw new AccessDeniedException(
                    "ADMIN users cannot be modified.");
        }

        // Prevent creation of another ADMIN through this endpoint.
        if (request.getRole() == RoleType.ADMIN) {
            throw new AccessDeniedException(
                    "ADMIN role cannot be assigned through this endpoint.");
        }

        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }

    @Override
    public UserResponse updateUserStatus(
            Long id,
            UpdateUserStatusRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User with Id: " + id + " not found"));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        // Only ADMIN can change user status.
        if (!isAdmin) {
            throw new AccessDeniedException(
                    "Only ADMIN can change user status.");
        }

        // ADMIN users cannot be modified.
        if (user.getRole() == RoleType.ADMIN) {
            throw new AccessDeniedException(
                    "ADMIN users cannot be modified.");
        }

        user.setEnabled(request.getEnabled());

        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }
}