package com.mrm.meetingroommanager.dto.mapper;

import com.mrm.meetingroommanager.dto.RoomDTO;
import com.mrm.meetingroommanager.entities.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomDTO toDTO(Room room) {
        if (room == null) {
            return null;
        }

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        roomDTO.setCapacity(room.getCapacity());
        roomDTO.setLocation(room.getLocation());

        return roomDTO;
    }

    public Room toEntity(RoomDTO roomDTO) {
        if (roomDTO == null) {
            return null;
        }

        Room room = new Room();
        room.setId(roomDTO.getId());
        room.setName(roomDTO.getName());
        room.setCapacity(roomDTO.getCapacity());
        room.setLocation(roomDTO.getLocation());

        return room;
    }

    public void updateEntityFromDTO(RoomDTO roomDTO, Room room) {
        if (roomDTO == null || room == null) {
            return;
        }

        if (roomDTO.getName() != null) {
            room.setName(roomDTO.getName());
        }

        if (roomDTO.getCapacity() > 0) {
            room.setCapacity(roomDTO.getCapacity());
        }

        if (roomDTO.getLocation() != null) {
            room.setLocation(roomDTO.getLocation());
        }
    }
}
