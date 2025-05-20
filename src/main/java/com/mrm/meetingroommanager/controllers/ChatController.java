package com.mrm.meetingroommanager.controllers;

import com.mrm.meetingroommanager.dto.ChatDTO;
import com.mrm.meetingroommanager.entities.Reservation;
import com.mrm.meetingroommanager.entities.Room;
import com.mrm.meetingroommanager.entities.User;
import com.mrm.meetingroommanager.services.AIService;
import com.mrm.meetingroommanager.services.ReservationService;
import com.mrm.meetingroommanager.services.RoomService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/chat")
public class ChatController {

    private final AIService aiService;
    private final RoomService roomService;
    private final ReservationService reservationService;

    public ChatController(AIService aiService, RoomService roomService, ReservationService reservationService) {
        this.aiService = aiService;
        this.roomService = roomService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public String showChatPage(Model model) {
        model.addAttribute("chatDTO", new ChatDTO());
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("content", "chat");
        return "layout";
    }

    @PostMapping("/send")
    @ResponseBody
    public ChatDTO sendMessage(@RequestBody ChatDTO chatDTO) {
        // Process the user's message through the AI service
        ChatDTO responseDTO = aiService.processUserMessage(chatDTO.getMessage());

        // Keep the original user message
        responseDTO.setMessage(chatDTO.getMessage());

        return responseDTO;
    }

    @PostMapping("/book")
    @ResponseBody
    public ChatDTO bookRoom(@RequestBody ChatDTO chatDTO) {
        try {
            // Get current authenticated user
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = null;

            if (auth != null && auth.getPrincipal() instanceof UserDetails) {
                // Note: In a real implementation, you would get the User entity from your UserService
                // based on the username: ((UserDetails) auth.getPrincipal()).getUsername()
                // This is a simplified version
                currentUser = new User();
                currentUser.setId(1L); // Replace with actual user ID retrieval
            }

            if (currentUser == null) {
                chatDTO.setResponse("You must be logged in to book a room.");
                chatDTO.setShowBookButton(false);
                return chatDTO;
            }

            // Get the room
            Room room;
            try {
                room = roomService.getRoomById(chatDTO.getSuggestedRoomId());
            } catch (Exception e) {
                chatDTO.setResponse("Sorry, the selected room is not available.");
                chatDTO.setShowBookButton(false);
                return chatDTO;
            }

            // Check if room is available for the specified time
            List<Reservation> conflictingReservations = reservationService.findByRoomId(room.getId()).stream()
                    .filter(res ->
                            (chatDTO.getSuggestedStartTime().isBefore(res.getEndTime()) &&
                                    chatDTO.getSuggestedEndTime().isAfter(res.getStartTime())))
                    .toList();

            if (!conflictingReservations.isEmpty()) {
                chatDTO.setResponse("Sorry, the room is already booked for the requested time slot.");
                chatDTO.setShowBookButton(false);
                return chatDTO;
            }

            // Create a new reservation
            Reservation reservation = new Reservation();
            reservation.setRoom(room);
            reservation.setUser(currentUser);
            reservation.setStartTime(chatDTO.getSuggestedStartTime());
            reservation.setEndTime(chatDTO.getSuggestedEndTime());
            reservation.setTitle(chatDTO.getSuggestedTitle());

            // Save the reservation
            reservationService.createReservation(reservation);

            // Update response
            chatDTO.setResponse("Great! I've booked " + room.getName() +
                    " for you from " + chatDTO.getSuggestedStartTime() +
                    " to " + chatDTO.getSuggestedEndTime() +
                    ". Your reservation title is: " + chatDTO.getSuggestedTitle());
            chatDTO.setShowBookButton(false);

            return chatDTO;
        } catch (Exception e) {
            chatDTO.setResponse("Sorry, there was an error booking the room: " + e.getMessage());
            chatDTO.setShowBookButton(false);
            return chatDTO;
        }
    }
}
