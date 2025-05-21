package com.moviebookingapp.movie_booking_app.kafka;

import com.moviebookingapp.movie_booking_app.Entity.Movie;
import com.moviebookingapp.movie_booking_app.Entity.Ticket;
import com.moviebookingapp.movie_booking_app.Repository.MovieRepository;
import com.moviebookingapp.movie_booking_app.Repository.TicketRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class KafkaConsumerService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MovieRepository movieRepository;

    @KafkaListener(topics = "ticket-booking-status-topic", groupId = "movie-booking-group")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);

        // Example message: "Avengers|INOX"
        String[] parts = message.split("\\|");
        if (parts.length != 2) {
            System.err.println("Invalid message format");
            return;
        }

        String movieName = parts[0];
        String theatreName = parts[1];

        // Get all booked tickets for the movie
        List<Ticket> tickets = ticketRepository.findByMovieNameAndTheatreName(movieName, theatreName);
        int totalBooked = tickets.stream().mapToInt(Ticket::getNumberOfTickets).sum();

        // Get the movie record
        Movie movie = movieRepository.findByMovieNameAndTheatreName(movieName, theatreName);
        if (movie == null) {
            System.err.println("Movie not found");
            return;
        }

        // Update movie status based on tickets booked
        int totalAvailable = movie.getTotalTickets() - totalBooked;
        if (totalAvailable <= 0) {
            movie.setStatus("SOLD OUT");
        } else {
            movie.setStatus("BOOK ASAP");
        }

        movieRepository.save(movie);
        System.out.println("Updated movie status to: " + movie.getStatus());
    }
}


