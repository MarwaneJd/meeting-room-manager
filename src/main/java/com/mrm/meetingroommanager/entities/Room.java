package com.mrm.meetingroommanager.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Entity
public class Room {
    @Id
    // Don't use any automatic generation strategy
    private Long id;

    @NotBlank(message = "Room name is required")
    private String name;
    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;
    private String location;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public int getCapacity() { return capacity; }
    public String getLocation() { return location; }
    public List<Reservation> getReservations() { return reservations; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public void setLocation(String location) { this.location = location; }
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
