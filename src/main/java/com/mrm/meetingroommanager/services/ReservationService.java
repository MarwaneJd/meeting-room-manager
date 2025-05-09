package com.mrm.meetingroommanager.services;

import com.mrm.meetingroommanager.entities.Reservation;
import com.mrm.meetingroommanager.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> findByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }

    public List<Reservation> findByRoomId(Long roomId) {
        return reservationRepository.findByRoomId(roomId);
    }

    public Reservation createReservation(Reservation reservation) {
        // For new reservations, set a high ID to avoid conflicts
        if (reservation.getId() == null) {
            // Get the highest ID currently in the database and add 1000
            Long highestId = reservationRepository.findAll().stream()
                    .map(Reservation::getId)
                    .max(Long::compareTo)
                    .orElse(0L);

            // Set the new ID to be at least 2000 or 1000 more than the highest existing ID
            reservation.setId(Math.max(2000L, highestId + 1000));
        }

        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}

