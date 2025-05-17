package com.moviebookingapp.movie_booking_app.Service;


import com.moviebookingapp.movie_booking_app.Entity.User;
import com.moviebookingapp.movie_booking_app.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User register(User user) {
        if (userRepository.existsById(user.getLoginId()) || userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User already exists");
        }
        return userRepository.save(user);
    }

    public User login(String loginId, String password) {
        return userRepository.findByLoginIdAndPassword(loginId, password);
    }

    public User forgotPassword(String loginId) {
        return userRepository.findById(loginId).orElse(null);
    }
}

