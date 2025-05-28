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
        String[] parts = message.split("\\|");
        if (parts.length != 2) return;

        String movieName = parts[0];
        String theatreName = parts[1];

        List<Ticket> tickets = ticketRepository.findByMovieNameAndTheatreName(movieName, theatreName);
        int totalBooked = tickets.stream().mapToInt(Ticket::getNumberOfTickets).sum();

        Movie movie = movieRepository.findByMovieNameAndTheatreName(movieName, theatreName);
        if (movie != null) {
            int remaining = movie.getTotalTickets() - totalBooked;
            movie.setStatus(remaining <= 0 ? "SOLD OUT" : "BOOK ASAP");
            movieRepository.save(movie);
        }
    }
}
