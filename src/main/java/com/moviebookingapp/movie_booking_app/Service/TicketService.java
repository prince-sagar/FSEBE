package com.moviebookingapp.movie_booking_app.Service;

import com.moviebookingapp.movie_booking_app.Entity.Movie;
import com.moviebookingapp.movie_booking_app.Entity.Ticket;
import com.moviebookingapp.movie_booking_app.Repository.MovieRepository;
import com.moviebookingapp.movie_booking_app.Repository.TicketRepository;
import com.moviebookingapp.movie_booking_app.kafka.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public Ticket bookTicket(Ticket ticket) {
        // Check if movie exists
        Movie movie = movieRepository.findByMovieNameAndTheatreName(ticket.getMovieName(), ticket.getTheatreName());
        if (movie == null) {
            throw new RuntimeException("Movie not found.");
        }

        // Calculate total booked
        int totalBooked = ticketRepository
                .findByMovieNameAndTheatreName(ticket.getMovieName(), ticket.getTheatreName())
                .stream()
                .mapToInt(Ticket::getNumberOfTickets)
                .sum();

        int remaining = movie.getTotalTickets() - totalBooked;

        if (ticket.getNumberOfTickets() > remaining) {
            throw new RuntimeException("Not enough tickets available.");
        }

        Ticket saved = ticketRepository.save(ticket);

        // Notify Kafka
        kafkaProducerService.sendTicketStatusUpdate(ticket.getMovieName(), ticket.getTheatreName());

        return saved;
    }

    public List<Ticket> getTickets(String movieName, String theatreName) {
        return ticketRepository.findByMovieNameAndTheatreName(movieName, theatreName);
    }

    public Ticket updateTicket(String id, Ticket updated) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));

        ticket.setNumberOfTickets(updated.getNumberOfTickets());
        ticket.setSeatNumber(updated.getSeatNumber());
        ticket.setTheatreName(updated.getTheatreName());
        ticket.setMovieName(updated.getMovieName());

        Ticket saved = ticketRepository.save(ticket);

        // Notify Kafka on update too
        kafkaProducerService.sendTicketStatusUpdate(updated.getMovieName(), updated.getTheatreName());

        return saved;
    }
}
