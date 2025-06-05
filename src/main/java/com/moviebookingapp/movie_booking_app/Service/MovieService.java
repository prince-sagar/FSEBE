package com.moviebookingapp.movie_booking_app.Service;

import com.moviebookingapp.movie_booking_app.Entity.Movie;
import com.moviebookingapp.movie_booking_app.Repository.MovieRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MovieService {
  @Autowired private MovieRepository movieRepository;

  @Cacheable(value = "movies")
  public List<Movie> getAllMovies() {
    return movieRepository.findAll();
  }

  @Cacheable(value = "movie", key = "#name")
  public List<Movie> searchMovies(String name) {
    return movieRepository.findByMovieNameContainingIgnoreCase(name);
  }

  public Movie getMovie(String movieName, String theatreName) {
    return movieRepository.findByMovieNameAndTheatreName(movieName, theatreName);
  }

  public Movie saveOrUpdate(Movie movie) {
    return movieRepository.save(movie);
  }

  public void deleteMovie(String id) {
    movieRepository.deleteById(id);
  }
}
