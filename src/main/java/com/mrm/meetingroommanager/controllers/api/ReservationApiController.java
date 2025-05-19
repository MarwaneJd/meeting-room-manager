package com.mrm.meetingroommanager.controllers.api;

import com.mrm.meetingroommanager.dto.ReservationDTO;
import com.mrm.meetingroommanager.dto.mapper.ReservationMapper;
import com.mrm.meetingroommanager.entities.Reservation;
import com.mrm.meetingroommanager.entities.User;
import com.mrm.meetingroommanager.services.ReservationService;
import com.mrm.meetingroommanager.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST API controller for Reservation management.
 * Provides endpoints for CRUD operations on reservations.
 */
@RestController
@RequestMapping("/api/reservations")
public class ReservationApiController {

    private final ReservationService reservationService;
    private final UserService userService;
    private final ReservationMapper reservationMapper;

    public ReservationApiController(ReservationService reservationService,
                                    UserService userService,
                                    ReservationMapper reservationMapper) {
        this.reservationService = reservationService;
        this.userService = userService;
        this.reservationMapper = reservationMapper;
    }

    /**
     * Get all reservations
     * @return List of reservation DTOs
     */
    @GetMapping
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> reservations = reservationService.getAllReservations().stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    /**
     * Get a reservation by ID
     * @param id Reservation ID
     * @return Reservation DTO or 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id)
                .map(reservationMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Create a new reservation
     * @param reservationDTO Reservation data
     * @param userDetails Authenticated user details
     * @return Created reservation DTO
     */
    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(
            @Valid @RequestBody ReservationDTO reservationDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        Reservation reservation = reservationMapper.toEntity(reservationDTO);

        // Set the current authenticated user as the reservation owner
        if (userDetails != null) {
            userService.findByUsername(userDetails.getUsername())
                    .ifPresent(reservation::setUser);
        }

        Reservation savedReservation = reservationService.createReservation(reservation);
        return new ResponseEntity<>(reservationMapper.toDTO(savedReservation), HttpStatus.CREATED);
    }

    /**
     * Update an existing reservation
     * @param id Reservation ID
     * @param reservationDTO Updated reservation data
     * @param userDetails Authenticated user details
     * @return Updated reservation DTO or 404 if not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody ReservationDTO reservationDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        return reservationService.getReservationById(id)
                .map(existingReservation -> {
                    // Check if the user is the owner or an admin
                    if (userDetails != null) {
                        User currentUser = userService.findByUsername(userDetails.getUsername()).orElse(null);
                        boolean isOwner = existingReservation.getUser() != null &&
                                currentUser != null &&
                                existingReservation.getUser().getId().equals(currentUser.getId());
                        boolean isAdmin = currentUser != null &&
                                currentUser.getRole() != null &&
                                "ADMIN".equals(currentUser.getRole().getName());

                        if (!isOwner && !isAdmin) {
                            return new ResponseEntity<ReservationDTO>(HttpStatus.FORBIDDEN);
                        }
                    }

                    reservationMapper.updateEntityFromDTO(reservationDTO, existingReservation);
                    Reservation updatedReservation = reservationService.createReservation(existingReservation);
                    return ResponseEntity.ok(reservationMapper.toDTO(updatedReservation));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Delete a reservation
     * @param id Reservation ID
     * @param userDetails Authenticated user details
     * @return 204 No Content if successful, 404 if not found, or 403 if forbidden
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        // First, check if the reservation exists
        Optional<Reservation> reservationOpt = reservationService.getReservationById(id);
        if (reservationOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Reservation existingReservation = reservationOpt.get();

        // Check if the user is authorized to delete the reservation
        if (userDetails != null) {
            User currentUser = userService.findByUsername(userDetails.getUsername()).orElse(null);
            boolean isOwner = existingReservation.getUser() != null &&
                    currentUser != null &&
                    existingReservation.getUser().getId().equals(currentUser.getId());
            boolean isAdmin = currentUser != null &&
                    currentUser.getRole() != null &&
                    "ADMIN".equals(currentUser.getRole().getName());

            if (!isOwner && !isAdmin) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }

        // User is authorized, proceed with deletion
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get reservations for the current authenticated user
     * @param userDetails Authenticated user details
     * @return List of reservation DTOs
     */
    @GetMapping("/user")
    public ResponseEntity<List<ReservationDTO>> getCurrentUserReservations(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails != null) {
            return userService.findByUsername(userDetails.getUsername())
                    .map(user -> {
                        List<ReservationDTO> reservations = reservationService.getReservationsByUser(user.getId()).stream()
                                .map(reservationMapper::toDTO)
                                .collect(Collectors.toList());
                        return ResponseEntity.ok(reservations);
                    })
                    .orElseGet(() -> ResponseEntity.ok(List.of()));
        }

        return ResponseEntity.ok(List.of());
    }

    /**
     * Get reservations for a specific room
     * @param roomId Room ID
     * @return List of reservation DTOs
     */
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<ReservationDTO>> getReservationsByRoom(@PathVariable Long roomId) {
        List<ReservationDTO> reservations = reservationService.getReservationsByRoom(roomId).stream()
                .map(reservationMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }
}
