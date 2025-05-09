package com.mrm.meetingroommanager.controllers;

import com.mrm.meetingroommanager.entities.Reservation;
import com.mrm.meetingroommanager.entities.Room;
import com.mrm.meetingroommanager.services.ReservationService;
import com.mrm.meetingroommanager.services.RoomService;
import com.mrm.meetingroommanager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final RoomService roomService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, RoomService roomService, UserService userService) {
        this.reservationService = reservationService;
        this.roomService = roomService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllReservations(Model model,
                                     @RequestParam(required = false) Boolean deleted,
                                     @RequestParam(required = false) String roomName) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservations", reservations);

        // Add success message for reservation deletion
        if (deleted != null && deleted) {
            String message = "Reservation for room " + (roomName != null ? "'" + roomName + "'" : "") + " has been successfully deleted.";
            model.addAttribute("successMessage", message);
        }

        model.addAttribute("content", "reservations :: content");
        return "layout";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("rooms", rooms);
        model.addAttribute("content", "reservation-form :: content");
        return "layout";
    }

    @PostMapping
    public String createReservation(@Valid @ModelAttribute("reservation") Reservation reservation,
                                    BindingResult result, Model model,
                                    @org.springframework.security.core.annotation.AuthenticationPrincipal org.springframework.security.core.userdetails.UserDetails userDetails) {
        if (result.hasErrors()) {
            model.addAttribute("rooms", roomService.getAllRooms());
            model.addAttribute("content", "reservation-form :: content");
            return "layout";
        }

        // Set the current authenticated user as the reservation owner
        if (userDetails != null) {
            userService.findByUsername(userDetails.getUsername())
                    .ifPresent(currentUser -> reservation.setUser(currentUser));
        }

        reservationService.createReservation(reservation);
        return "redirect:/reservations";
    }

    @GetMapping("/{id}")
    public String getReservationById(@PathVariable Long id, Model model) {
        reservationService.getReservationById(id).ifPresent(reservation -> {
            model.addAttribute("reservation", reservation);
        });
        model.addAttribute("content", "reservation-detail :: content");
        return "layout";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        reservationService.getReservationById(id).ifPresent(reservation -> {
            model.addAttribute("reservation", reservation);
            model.addAttribute("rooms", roomService.getAllRooms());
        });
        model.addAttribute("content", "reservation-form :: content");
        return "layout";
    }

    @PostMapping("/{id}")
    public String updateReservation(@PathVariable Long id, @Valid @ModelAttribute("reservation") Reservation reservation,
                                    BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("rooms", roomService.getAllRooms());
            model.addAttribute("content", "reservation-form :: content");
            return "layout";
        }
        reservationService.createReservation(reservation);
        return "redirect:/reservations";
    }

    @GetMapping("/{id}/delete")
    public String deleteReservation(@PathVariable Long id) {
        // Get the reservation and extract room name if available
        String roomName = "";
        Optional<Reservation> reservationOpt = reservationService.getReservationById(id);
        if (reservationOpt.isPresent()) {
            Reservation reservation = reservationOpt.get();
            if (reservation.getRoom() != null) {
                roomName = reservation.getRoom().getName();
            }
        }

        // Delete the reservation
        reservationService.deleteReservation(id);

        // Add success message to redirect attributes
        return "redirect:/reservations?deleted=true&roomName=" + roomName;
    }
}
