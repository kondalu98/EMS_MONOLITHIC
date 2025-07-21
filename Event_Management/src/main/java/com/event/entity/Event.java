package com.event.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity
@Table(name = "event")
public class  Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventID;
    private String name;
    private String category;
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String organizerID;

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getOrganizerID() {
        return organizerID;
    }

    public void setOrganizerID(String organizerID) {
        this.organizerID = organizerID;
    }

    public Event(Long eventID, String name, String category, String location, LocalDate date, String organizer) {
        this.eventID = eventID;
        this.name = name;
        this.category = category;
        this.location = location;
        this.date = date;
        this.organizerID = organizer;
    }

    public Event()
    {

    }
    @Override
    public String toString() {
        return "Event{" +
                "eventID=" + eventID +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", organizerID='" + organizerID + '\'' +
                '}';
    }
}
