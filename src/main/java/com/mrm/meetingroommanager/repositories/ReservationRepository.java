package com.mrm.meetingroommanager.repositories;

import com.mrm.meetingroommanager.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByRoomId(Long roomId);
    List<Reservation> findByUserId(Long userId);
}
