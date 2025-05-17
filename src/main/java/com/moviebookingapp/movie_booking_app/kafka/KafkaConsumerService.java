package com.moviebookingapp.movie_booking_app.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "ticket-booking-status-topic", groupId = "movie-booking-group")
    public void consume(String message) {
        System.out.println("Consumed message: " + message);
        // Here, you could add logic to update ticket availability/status
    }
}

