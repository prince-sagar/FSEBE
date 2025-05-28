package com.moviebookingapp.movie_booking_app.Controller;

import com.moviebookingapp.movie_booking_app.Entity.Ticket;
import com.moviebookingapp.movie_booking_app.kafka.KafkaProducerService;
import com.moviebookingapp.movie_booking_app.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1.0/moviebooking")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/{movieName}/add")
    public ResponseEntity<?> bookTicket(@PathVariable String movieName, @RequestBody Ticket ticket) {
        Ticket booked = ticketService.bookTicket(ticket);
        kafkaProducerService.sendTicketStatusUpdate(movieName, ticket.getTheatreName());
        return ResponseEntity.ok(booked);
    }

    @GetMapping("/{movieName}/tickets/{theatreName}")
    public ResponseEntity<?> getTickets(@PathVariable String movieName, @PathVariable String theatreName) {
        return ResponseEntity.ok(ticketService.getTickets(movieName, theatreName));
    }

    @PutMapping("/{movieName}/update/{ticketId}")
    public ResponseEntity<?> updateTicket(@PathVariable String movieName, @PathVariable String ticketId, @RequestBody Ticket updated) {
        return ResponseEntity.ok(ticketService.updateTicket(ticketId, updated));
    }
}


