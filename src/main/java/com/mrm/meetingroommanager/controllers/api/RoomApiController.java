package com.mrm.meetingroommanager.controllers.api;

import com.mrm.meetingroommanager.dto.RoomDTO;
import com.mrm.meetingroommanager.dto.mapper.RoomMapper;
import com.mrm.meetingroommanager.entities.Room;
import com.mrm.meetingroommanager.entities.User;
import com.mrm.meetingroommanager.exception.ResourceNotFoundException;
import com.mrm.meetingroommanager.services.RoomService;
import com.mrm.meetingroommanager.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * REST API controller for Room management.
 * Provides endpoints for CRUD operations on rooms.
 */
@RestController
@RequestMapping("/api/rooms")
public class RoomApiController {

    private final RoomService roomService;
    private final RoomMapper roomMapper;
    private final UserService userService;

    public RoomApiController(RoomService roomService, RoomMapper roomMapper, UserService userService) {
        this.roomService = roomService;
        this.roomMapper = roomMapper;
        this.userService = userService;
    }

    /**
     * Get all rooms
     * @return List of room DTOs
     */
    @GetMapping
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        List<RoomDTO> rooms = roomService.getAllRooms().stream()
                .map(roomMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    /**
     * Get a room by ID
     * @param id Room ID
     * @return Room DTO or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomDTO> getRoomById(@PathVariable Long id) {
        try {
            Room room = roomService.getRoomById(id);
            return ResponseEntity.ok(roomMapper.toDTO(room));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create a new room
     * @param roomDTO Room data
     * @param userDetails Authenticated user details
     * @return Created room DTO
     */
    @PostMapping
    public ResponseEntity<RoomDTO> createRoom(
            @Valid @RequestBody RoomDTO roomDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Check if user is admin
        if (userDetails != null) {
            User currentUser = userService.findByUsername(userDetails.getUsername()).orElse(null);
            boolean isAdmin = currentUser != null &&
                    currentUser.getRole() != null &&
                    "ADMIN".equals(currentUser.getRole().getName());

            if (!isAdmin) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Room room = roomMapper.toEntity(roomDTO);
        Room savedRoom = roomService.createRoom(room);
        return new ResponseEntity<>(roomMapper.toDTO(savedRoom), HttpStatus.CREATED);
    }

    /**
     * Update an existing room
     * @param id Room ID
     * @param roomDTO Updated room data
     * @param userDetails Authenticated user details
     * @return Updated room DTO or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoomDTO> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomDTO roomDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Check if user is admin
        if (userDetails != null) {
            User currentUser = userService.findByUsername(userDetails.getUsername()).orElse(null);
            boolean isAdmin = currentUser != null &&
                    currentUser.getRole() != null &&
                    "ADMIN".equals(currentUser.getRole().getName());

            if (!isAdmin) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            // Get the existing room
            Room existingRoom = roomService.getRoomById(id);

            // Update the room with values from the DTO
            roomMapper.updateEntityFromDTO(roomDTO, existingRoom);

            // Save the updated room
            Room updatedRoom = roomService.updateRoom(id, existingRoom);

            return ResponseEntity.ok(roomMapper.toDTO(updatedRoom));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete a room
     * @param id Room ID
     * @param userDetails Authenticated user details
     * @return 204 No Content if successful, 404 if not found, or 403 if forbidden
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        // Check if user is admin
        if (userDetails != null) {
            User currentUser = userService.findByUsername(userDetails.getUsername()).orElse(null);
            boolean isAdmin = currentUser != null &&
                    currentUser.getRole() != null &&
                    "ADMIN".equals(currentUser.getRole().getName());

            if (!isAdmin) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            roomService.getRoomById(id); // Check if room exists
            roomService.deleteRoom(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get rooms with a minimum capacity
     * @param minCapacity Minimum capacity required
     * @return List of room DTOs meeting the capacity requirement
     */
    @GetMapping("/capacity/{minCapacity}")
    public ResponseEntity<List<RoomDTO>> getRoomsByMinCapacity(@PathVariable int minCapacity) {
        // Get rooms that meet the minimum capacity requirement
        List<Room> filteredRooms = roomService.getRoomsByMinCapacity(minCapacity);

        // Convert entities to DTOs
        List<RoomDTO> roomDTOs = filteredRooms.stream()
                .map(roomMapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(roomDTOs);
    }

    /**
     * Get available rooms for a specific time period
     * @param startTime Start time in ISO format
     * @param endTime End time in ISO format
     * @return List of available room DTOs
     */
    @GetMapping("/available")
    public ResponseEntity<List<RoomDTO>> getAvailableRooms(
            @RequestParam String startTime,
            @RequestParam String endTime) {
        // This endpoint would require additional implementation in the service layer
        // to check room availability based on existing reservations
        List<RoomDTO> rooms = roomService.getAllRooms().stream()
                .map(roomMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }
}
