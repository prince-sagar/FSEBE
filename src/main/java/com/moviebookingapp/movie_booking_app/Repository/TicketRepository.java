package com.moviebookingapp.movie_booking_app.Repository;

import com.moviebookingapp.movie_booking_app.Entity.Ticket;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String> {
  List<Ticket> findByMovieNameAndTheatreName(String movieName, String theatreName);

  List<Ticket> findSeatsByMovieNameAndTheatreName(String movieName, String theatreName);
}
