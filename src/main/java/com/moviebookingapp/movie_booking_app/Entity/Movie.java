package com.moviebookingapp.movie_booking_app.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "movies")
public class Movie {
    @Id
    private String id;
    private String movieName;
    private String theatreName;
    private int totalTickets;
    private String status;  // e.g. "BOOK ASAP", "SOLD OUT"
}

