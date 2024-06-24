package com.ticketbooking.ticketbooking.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ticketbooking.ticketbooking.model.Event;
import com.ticketbooking.ticketbooking.repository.EventRepository;



@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents(@RequestParam(required = false) String name) {
        try {
            List<Event> events = new ArrayList<Event>();
            if (name == null)
                eventRepository.findAll().forEach(events::add);
            else
                eventRepository.findByNameContaining(name).forEach(events::add);

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") long id) {
        Optional<Event> eventData = eventRepository.findById(id);

        if (eventData.isPresent()) {
            return new ResponseEntity<>(eventData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        try {
            Event _event = eventRepository
                    .save(new Event(event.getName(), event.getDescription(), event.getDate(), event.getPrice()));
            return new ResponseEntity<>(_event, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/events/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") long id, @RequestBody Event event) {
        Optional<Event> eventData = eventRepository.findById(id);

        if (eventData.isPresent()) {
            Event _event = eventData.get();
            _event.setName(event.getName());
            _event.setDescription(event.getDescription());
            _event.setDate(event.getDate());
            _event.setPrice(event.getPrice());
            return new ResponseEntity<>(eventRepository.save(_event), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
