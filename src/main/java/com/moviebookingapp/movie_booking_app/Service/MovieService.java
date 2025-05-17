package com.moviebookingapp.movie_booking_app.Service;

import com.moviebookingapp.movie_booking_app.Entity.Movie;
import com.moviebookingapp.movie_booking_app.Repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> searchMovies(String name) {
        return movieRepository.findByMovieNameContainingIgnoreCase(name);
    }

    public Movie getMovie(String movieName, String theatreName) {
        return movieRepository.findByMovieNameAndTheatreName(movieName, theatreName);
    }

    public Movie updateMovie(Movie movie) {
        return movieRepository.save(movie);
    }
}

