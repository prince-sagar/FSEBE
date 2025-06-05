package com.moviebookingapp.movie_booking_app.Controller;

import com.moviebookingapp.movie_booking_app.Entity.Movie;
import com.moviebookingapp.movie_booking_app.Service.MovieService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/moviebooking")
public class MovieController {

  @Autowired private MovieService movieService;

  @GetMapping("/all")
  public List<Movie> getAllMovies() {
    return movieService.getAllMovies();
  }

  @GetMapping("/movies/search/{movieName}")
  public List<Movie> searchMovies(@PathVariable String movieName) {
    return movieService.searchMovies(movieName);
  }
}
