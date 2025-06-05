package com.moviebookingapp.movie_booking_app.Repository;

import com.moviebookingapp.movie_booking_app.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
  boolean existsByEmail(String email);

  User findByLoginIdAndPassword(String loginId, String password);
}
