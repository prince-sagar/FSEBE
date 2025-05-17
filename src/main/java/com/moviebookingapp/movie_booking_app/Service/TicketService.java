package com.moviebookingapp.movie_booking_app.Service;

import com.moviebookingapp.movie_booking_app.Entity.Ticket;
import com.moviebookingapp.movie_booking_app.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public Ticket bookTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public List<Ticket> getTicketsForMovie(String movieName, String theatreName) {
        return ticketRepository.findByMovieNameAndTheatreName(movieName, theatreName);
    }
}

