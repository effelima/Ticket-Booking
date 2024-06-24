package com.ticketbooking.ticketbooking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ticketbooking.ticketbooking.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.ticketbooking.ticketbooking.controller.EventController;
import com.ticketbooking.ticketbooking.model.Event;
import com.ticketbooking.ticketbooking.repository.EventRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(EventController.class)
public class EventControllerTests {

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private LocalDate eventDate = LocalDate.of(2024, 8, 9);
    private String eventName = "Vans Warped Tour";
    private float eventPrice = 250.99f;
    private String eventDescription = "The Warped Tour began as a skate-punk music tour with" +
            "third-wave ska music heavily featured in the lineups. Eventually, the music" +
            "morphed to feature predominantly pop punk and extreme metalcore. Because the" +
            "festival traveled to various sites, the concert layout changed with each venue.";

    @Test
    void shouldCreateEvent() throws Exception {
        Event event = new Event(this.eventName, this.eventDescription, this.eventDate, this.eventPrice);
        mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void shouldReturnEvent() throws Exception {
        long id = 1L;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        Event event = new Event(id, this.eventName, this.eventDescription, this.eventDate, this.eventPrice);

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        mockMvc.perform(get("/api/events/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(event.getName()))
                .andExpect(jsonPath("$.description").value(event.getDescription()))
                .andExpect(jsonPath("$.date").value(event.getDate().format(formatter)))
                .andExpect(jsonPath("$.price").value(event.getPrice()))
                .andDo(print());
    }

    @Test
    void shouldReturnNotFoundEvent() throws Exception {
        long id = 1L;

        when(eventRepository.findById(id)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/events/{id}"))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void shouldReturnListOfEvents() throws Exception {
        List<Event> eventList = new ArrayList<>();
        for (int i = 1; i<=3; i++) {
            eventList.add(new Event(
                    i,
                    this.eventName + " | " + i,
                    this.eventDescription,
                    this.eventDate,
                    this.eventPrice
            ));
        }

        when(eventRepository.findAll()).thenReturn(eventList);
        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(eventList.size()))
                .andDo(print());
    }

    @Test
    void shouldReturnListOfEventsWithFilter() throws Exception {
        List<Event> eventList = new ArrayList<>();
        for (int i = 1; i<=3; i++) {
            eventList.add(new Event(
                    i,
                    this.eventName + " | " + i,
                    this.eventDescription,
                    this.eventDate,
                    this.eventPrice
            ));
        }

        String eventName = " Tour 2";
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("name", eventName);

        when(eventRepository.findByNameContaining(eventName)).thenReturn(eventList);
        mockMvc.perform(get("/api/events").params(paramsMap))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(eventList.size()))
                .andDo(print());
    }

    @Test
    void shouldReturnNoContentWhenFilter() throws Exception {
        String eventName = "Rock in Rio";
        MultiValueMap<String, String> paramsMap = new LinkedMultiValueMap<>();
        paramsMap.add("name", eventName);

        List<Event> events = Collections.emptyList();

        when(eventRepository.findByNameContaining(eventName)).thenReturn(events);
        mockMvc.perform(get("/api/events").params(paramsMap))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void shouldUpdateEvent() throws Exception {
        long id = 1L;

        Event event = new Event(id, this.eventName, this.eventDescription, this.eventDate, this.eventPrice);
        Event updatedEvent = new Event(id, "Rock in Rio", "Rock in Rio 2024", this.eventDate, this.eventPrice);

        when(eventRepository.findById(id)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        mockMvc.perform(put("/api/events/{id}", id).contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEvent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updatedEvent.getName()))
                .andExpect(jsonPath("$.description").value(updatedEvent.getDescription()))
                .andDo(print());
    }
}













