package com.moviebookingapp.movie_booking_app.Controller;

import com.moviebookingapp.movie_booking_app.Entity.Ticket;
import com.moviebookingapp.movie_booking_app.kafka.KafkaProducerService;
import com.moviebookingapp.movie_booking_app.Service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/moviebooking")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/{movieName}/add")
    public ResponseEntity<?> bookTicket(@PathVariable String movieName, @RequestBody Ticket ticket) {
        if (!movieName.equals(ticket.getMovieName())) {
            return ResponseEntity.badRequest().body("Movie name mismatch");
        }
        Ticket bookedTicket = ticketService.bookTicket(ticket);
        kafkaProducerService.sendTicketStatusUpdate(movieName, ticket.getTheatreName());
        return ResponseEntity.ok(bookedTicket);
    }

    @GetMapping("/{movieName}/tickets/{theatreName}")
    public List<Ticket> getTickets(@PathVariable String movieName, @PathVariable String theatreName) {
        return ticketService.getTicketsForMovie(movieName, theatreName);
    }
}

