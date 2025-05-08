package com.mrm.meetingroommanager.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reservation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;

    @ManyToOne
    private User user;

    @ManyToOne
    private Room room;
}

