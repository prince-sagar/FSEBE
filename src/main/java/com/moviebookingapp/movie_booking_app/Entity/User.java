package com.moviebookingapp.movie_booking_app.Entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("users")
public class User {
    @Id
    private String loginId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String contactNumber;
    private String role = "ROLE_USER"; // default role
}
