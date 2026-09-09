package com.bash.dash.messaging;

import com.bash.dash.messaging.dtos.LocationMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class LocationProducer {

    private final RabbitTemplate rabbitTemplate;

    public LocationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(LocationMessage locationMessage) {

        rabbitTemplate.convertAndSend("locationQueue", locationMessage);
    }
}