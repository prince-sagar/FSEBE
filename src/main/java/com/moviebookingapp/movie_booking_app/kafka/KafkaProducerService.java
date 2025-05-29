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
        System.out.println("Sending Kafka message: " + message);
        kafkaTemplate.send(TOPIC, message).whenComplete((result, ex) -> {
            if (ex != null) {
                System.out.println("Kafka send failed: " + ex.getMessage());
            } else {
                System.out.println("Kafka send succeeded: " + result.getRecordMetadata().toString());
            }
        });
    }

}


