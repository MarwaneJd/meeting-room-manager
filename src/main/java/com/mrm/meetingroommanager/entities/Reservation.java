package com.mrm.meetingroommanager.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Reservation {
    @Id
    // Don't use any automatic generation strategy
    private Long id;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;

    @ManyToOne
    private User user;

    @ManyToOne
    private Room room;

    // Getters
    public Long getId() { return id; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getTitle() { return title; }
    public User getUser() { return user; }
    public Room getRoom() { return room; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setTitle(String title) { this.title = title; }
    public void setUser(User user) { this.user = user; }
    public void setRoom(Room room) { this.room = room; }
}

