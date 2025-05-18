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
        // For new rooms, set a high ID to avoid conflicts
        if (room.getId() == null) {
            // Get the highest ID currently in the database and add 1000
            Long highestId = roomRepository.findAll().stream()
                    .map(Room::getId)
                    .max(Long::compareTo)
                    .orElse(0L);

            // Set the new ID to be at least 2000 or 1000 more than the highest existing ID
            room.setId(Math.max(2000L, highestId + 1000));
        }

        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, Room updatedRoom) {
        return roomRepository.findById(id).map(room -> {
            room.setName(updatedRoom.getName());
            room.setCapacity(updatedRoom.getCapacity());
            room.setLocation(updatedRoom.getLocation());
            return roomRepository.save(room);
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + id));
    }

    /**
     * Get rooms with capacity greater than or equal to the specified minimum capacity
     * @param minCapacity the minimum capacity required
     * @return list of rooms meeting the capacity requirement
     */
    public List<Room> getRoomsByMinCapacity(int minCapacity) {
        return roomRepository.findAll().stream()
                .filter(room -> room.getCapacity() >= minCapacity)
                .toList();
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}

