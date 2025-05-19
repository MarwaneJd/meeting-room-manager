package com.mrm.meetingroommanager.dto.mapper;

import com.mrm.meetingroommanager.dto.ReservationDTO;
import com.mrm.meetingroommanager.entities.Reservation;
import com.mrm.meetingroommanager.entities.Room;
import com.mrm.meetingroommanager.entities.User;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    public ReservationDTO toDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getId());
        reservationDTO.setStartTime(reservation.getStartTime());
        reservationDTO.setEndTime(reservation.getEndTime());
        reservationDTO.setTitle(reservation.getTitle());

        if (reservation.getUser() != null) {
            reservationDTO.setUserId(reservation.getUser().getId());
            reservationDTO.setUsername(reservation.getUser().getUsername());
        }

        if (reservation.getRoom() != null) {
            reservationDTO.setRoomId(reservation.getRoom().getId());
            reservationDTO.setRoomName(reservation.getRoom().getName());
        }

        return reservationDTO;
    }

    public Reservation toEntity(ReservationDTO reservationDTO) {
        if (reservationDTO == null) {
            return null;
        }

        Reservation reservation = new Reservation();
        reservation.setId(reservationDTO.getId());
        reservation.setStartTime(reservationDTO.getStartTime());
        reservation.setEndTime(reservationDTO.getEndTime());
        reservation.setTitle(reservationDTO.getTitle());

        if (reservationDTO.getUserId() != null) {
            User user = new User();
            user.setId(reservationDTO.getUserId());
            reservation.setUser(user);
        }

        if (reservationDTO.getRoomId() != null) {
            Room room = new Room();
            room.setId(reservationDTO.getRoomId());
            reservation.setRoom(room);
        }

        return reservation;
    }

    public void updateEntityFromDTO(ReservationDTO reservationDTO, Reservation reservation) {
        if (reservationDTO == null || reservation == null) {
            return;
        }

        if (reservationDTO.getStartTime() != null) {
            reservation.setStartTime(reservationDTO.getStartTime());
        }

        if (reservationDTO.getEndTime() != null) {
            reservation.setEndTime(reservationDTO.getEndTime());
        }

        if (reservationDTO.getTitle() != null) {
            reservation.setTitle(reservationDTO.getTitle());
        }

        if (reservationDTO.getUserId() != null) {
            User user = new User();
            user.setId(reservationDTO.getUserId());
            reservation.setUser(user);
        }

        if (reservationDTO.getRoomId() != null) {
            Room room = new Room();
            room.setId(reservationDTO.getRoomId());
            reservation.setRoom(room);
        }
    }
}
