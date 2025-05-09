package com.mrm.meetingroommanager.controllers;

import com.mrm.meetingroommanager.entities.Room;
import com.mrm.meetingroommanager.services.RoomService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public String getAllRooms(Model model,
                              @RequestParam(required = false) Boolean accessDenied,
                              @RequestParam(required = false) Boolean deleted,
                              @RequestParam(required = false) String roomName,
                              @SessionAttribute(name = "accessDeniedMessage", required = false) String accessDeniedMessage,
                              @SessionAttribute(name = "org.springframework.web.servlet.support.SessionFlashMapManager.FLASH_MAPS", required = false) Object flashMaps) {
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);

        // Add access denied message if present
        if (accessDenied != null && accessDenied && accessDeniedMessage != null) {
            model.addAttribute("errorMessage", accessDeniedMessage);
            // Remove the message from the session to prevent it from showing again
            model.asMap().get("org.springframework.validation.BindingResult.room");
        }

        // Add success message for room deletion
        if (deleted != null && deleted) {
            String message = "Room " + (roomName != null ? "'" + roomName + "'" : "") + " has been successfully deleted.";
            model.addAttribute("successMessage", message);
        }

        model.addAttribute("content", "rooms :: content");
        return "layout";
    }

    @GetMapping("/{id}")
    public String getRoomById(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id);
        model.addAttribute("room", room);
        model.addAttribute("content", "room-detail :: content");
        return "layout";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("room", new Room());
        model.addAttribute("content", "room-form :: content");
        return "layout";
    }

    @PostMapping
    public String createRoom(@Valid @ModelAttribute("room") Room room, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("content", "room-form :: content");
            return "layout";
        }
        roomService.createRoom(room);
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id);
        model.addAttribute("room", room);
        model.addAttribute("content", "room-form :: content");
        return "layout";
    }

    @PostMapping("/{id}")
    public String updateRoom(@PathVariable Long id, @Valid @ModelAttribute("room") Room room,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("content", "room-form :: content");
            return "layout";
        }
        roomService.updateRoom(id, room);
        return "redirect:/rooms";
    }

    @GetMapping("/{id}/delete")
    public String deleteRoom(@PathVariable Long id, Model model) {
        Room room = roomService.getRoomById(id);
        String roomName = room.getName();
        roomService.deleteRoom(id);

        // Add success message to redirect attributes
        return "redirect:/rooms?deleted=true&roomName=" + roomName;
    }
}

