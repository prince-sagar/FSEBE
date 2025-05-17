package com.moviebookingapp.movie_booking_app.Repository;

import com.moviebookingapp.movie_booking_app.Entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByMovieNameAndTheatreName(String movieName, String theatreName);
}

