package com.ticketbooking.ticketbooking.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "price")
    private float price;

    public Event() {

    }

    public Event(String name, String description, LocalDate eventDate, float price) {
        this.name = name;
        this.description = description;
        this.date = eventDate;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
