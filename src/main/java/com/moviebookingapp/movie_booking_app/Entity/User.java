package com.moviebookingapp.movie_booking_app.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "users")
public class User {
    @Id
    private String loginId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String contactNumber;
}
