package com.ticketbooking.ticketbooking.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ticketbooking.ticketbooking.model.Event;


public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByNameContaining(String name);
}
