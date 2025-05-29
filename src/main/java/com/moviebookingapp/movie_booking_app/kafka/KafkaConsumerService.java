package com.moviebookingapp.movie_booking_app.kafka;

import com.moviebookingapp.movie_booking_app.Entity.Movie;
import com.moviebookingapp.movie_booking_app.Repository.MovieRepository;
import com.moviebookingapp.movie_booking_app.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
class KafkaConsumerService {

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

        Movie movie = movieRepository.findByMovieNameAndTheatreName(movieName, theatreName);
        if (movie != null) {
            movie.setStatus(movie.getTotalTickets() <= 0 ? "SOLD OUT" : "BOOK ASAP");
            movieRepository.save(movie);
        }
    }

}
