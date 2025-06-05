package com.moviebookingapp.movie_booking_app.Service;

import com.moviebookingapp.movie_booking_app.Entity.Movie;
import com.moviebookingapp.movie_booking_app.Entity.Ticket;
import com.moviebookingapp.movie_booking_app.Repository.MovieRepository;
import com.moviebookingapp.movie_booking_app.Repository.TicketRepository;
import com.moviebookingapp.movie_booking_app.kafka.KafkaProducerService;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

  @Autowired private TicketRepository ticketRepository;

  @Autowired private MovieRepository movieRepository;

  @Autowired private KafkaProducerService kafkaProducerService;

  public Ticket bookTicket(Ticket ticket) {
    // Check if movie exists
    Movie movie =
        movieRepository.findByMovieNameAndTheatreName(
            ticket.getMovieName(), ticket.getTheatreName());
    if (movie == null) {
      throw new RuntimeException("Movie not found.");
    }

    // Validate availability
    if (ticket.getNumberOfTickets() > movie.getTotalTickets()) {
      throw new RuntimeException("Not enough tickets available.");
    }

    // Validate seat number
    List<Ticket> bookedSeats =
        ticketRepository.findSeatsByMovieNameAndTheatreName(
            ticket.getMovieName(), ticket.getTheatreName());
    List<String> bookedSeatNumbers =
        bookedSeats.stream()
            .map(Ticket::getSeatNumber)
            .flatMap(seat -> Arrays.stream(seat.split(",")))
            .toList();
    String[] requestedSeats = ticket.getSeatNumber().split(",");
    for (String seat : requestedSeats) {
      if (bookedSeatNumbers.contains(seat)) {
        throw new RuntimeException("Seat " + seat + " is already booked.");
      }
    }

    // Subtract tickets and update movie
    movie.setTotalTickets(movie.getTotalTickets() - ticket.getNumberOfTickets());
    movieRepository.save(movie);

    // Save ticket
    Ticket saved = ticketRepository.save(ticket);

    // Send update to Kafka (for status update)
    kafkaProducerService.sendTicketStatusUpdate(ticket.getMovieName(), ticket.getTheatreName());

    return saved;
  }

  public List<Ticket> getTickets(String movieName, String theatreName) {
    return ticketRepository.findByMovieNameAndTheatreName(movieName, theatreName);
  }

  public Ticket updateTicket(String id, Ticket updated) {
    Ticket ticket =
        ticketRepository.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));

    ticket.setNumberOfTickets(updated.getNumberOfTickets());
    ticket.setSeatNumber(updated.getSeatNumber());
    ticket.setTheatreName(updated.getTheatreName());
    ticket.setMovieName(updated.getMovieName());

    Ticket saved = ticketRepository.save(ticket);

    // Notify Kafka on update too
    kafkaProducerService.sendTicketStatusUpdate(updated.getMovieName(), updated.getTheatreName());

    return saved;
  }


}
