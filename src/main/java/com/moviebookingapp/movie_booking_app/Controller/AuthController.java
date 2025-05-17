package com.moviebookingapp.movie_booking_app.Controller;

import com.moviebookingapp.movie_booking_app.Entity.User;
import com.moviebookingapp.movie_booking_app.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1.0/moviebooking")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User registeredUser = userService.register(user);
            return ResponseEntity.ok(registeredUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String loginId, @RequestParam String password) {
        User user = userService.login(loginId, password);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @GetMapping("/{loginId}/forgot")
    public ResponseEntity<?> forgotPassword(@PathVariable String loginId) {
        User user = userService.forgotPassword(loginId);
        if (user != null) {
            return ResponseEntity.ok("Your password is: " + user.getPassword());
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }
}

