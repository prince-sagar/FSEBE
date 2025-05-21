package com.moviebookingapp.movie_booking_app.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaProducerService {

    private static final String TOPIC = "ticket-booking-status-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendTicketStatusUpdate(String movieName, String theatreName) {
        String message = movieName + "|" + theatreName;
        kafkaTemplate.send(TOPIC, message);
        System.out.println("Sent Kafka message: " + message);
    }
}


