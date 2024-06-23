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

import com.ticketbooking.ticketbooking.controller.EventController;
import com.ticketbooking.ticketbooking.model.Event;
import com.ticketbooking.ticketbooking.repository.EventRepository;

@WebMvcTest(EventController.class)
public class EventControllerTests {

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateEvent() throws Exception {
        LocalDate eventDate = LocalDate.of(2024, 8, 9);
        String eventDescription = "The Warped Tour began as a skate-punk music tour with third-wave ska music heavily featured in the lineups. Eventually, the music morphed to feature predominantly pop punk and extreme metalcore. Because the festival traveled to various sites, the concert layout changed with each venue.";
        Event event = new Event("Vans Warped Tour", eventDescription, eventDate, 250.99f);
        mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andExpect(status().isCreated())
                .andDo(print());
    }
}













