package com.moviebookingapp.movie_booking_app.Controller;

import com.moviebookingapp.movie_booking_app.Entity.Movie;
import com.moviebookingapp.movie_booking_app.Entity.Ticket;
import com.moviebookingapp.movie_booking_app.Repository.MovieRepository;
import com.moviebookingapp.movie_booking_app.Repository.TicketRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/moviebooking")
public class AdminController {

  @Autowired private MovieRepository movieRepository;

  @Autowired private TicketRepository ticketRepository;

  @GetMapping("/{movieName}/tickets/{theatreName}/status")
  public ResponseEntity<?> getBookedTicketsAndUpdateStatus(
      @PathVariable String movieName, @PathVariable String theatreName) {
    List<Ticket> tickets = ticketRepository.findByMovieNameAndTheatreName(movieName, theatreName);
    int booked = tickets.stream().mapToInt(Ticket::getNumberOfTickets).sum();

    Movie movie = movieRepository.findByMovieNameAndTheatreName(movieName, theatreName);
    if (movie == null) return ResponseEntity.badRequest().body("Movie not found");

    int available = movie.getTotalTickets() - booked;
    movie.setStatus(available <= 0 ? "SOLD OUT" : "BOOK ASAP");
    movieRepository.save(movie);

    return ResponseEntity.ok(
        String.format(
            "Booked: %d, Available: %d, Status: %s", booked, available, movie.getStatus()));
  }

  @DeleteMapping("/{movieName}/delete/{id}")
  public ResponseEntity<?> deleteMovie(@PathVariable String movieName, @PathVariable String id) {
    return movieRepository
        .findById(id)
        .map(
            movie -> {
              if (!movie.getMovieName().equalsIgnoreCase(movieName)) {
                return ResponseEntity.badRequest().body("Movie name and ID mismatch");
              }
              movieRepository.delete(movie);
              return ResponseEntity.ok("Movie deleted");
            })
        .orElse(ResponseEntity.badRequest().body("Movie not found"));
  }
}
