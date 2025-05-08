package com.mrm.meetingroommanager.services;

import com.mrm.meetingroommanager.entities.Room;
import com.mrm.meetingroommanager.exception.ResourceNotFoundException;
import com.mrm.meetingroommanager.repositories.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + id));
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Optional<Room> updateRoom(Long id, Room updatedRoom) {
        return roomRepository.findById(id).map(room -> {
            room.setName(updatedRoom.getName());
            room.setCapacity(updatedRoom.getCapacity());
            room.setLocation(updatedRoom.getLocation());
            return roomRepository.save(room);
        });
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}

