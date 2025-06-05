package com.moviebookingapp.movie_booking_app.Repository;

import com.moviebookingapp.movie_booking_app.Entity.Movie;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
  List<Movie> findByMovieNameContainingIgnoreCase(String movieName);

  Movie findByMovieNameAndTheatreName(String movieName, String theatreName);
}
