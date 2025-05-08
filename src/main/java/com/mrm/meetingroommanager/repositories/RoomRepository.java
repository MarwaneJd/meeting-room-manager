package com.mrm.meetingroommanager.repositories;

import com.mrm.meetingroommanager.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {}

