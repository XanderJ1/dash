package com.bash.dash.messaging;

import com.bash.dash.messaging.dtos.RideMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class RideProducer {

    private final RabbitTemplate rabbitTemplate;

    public RideProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(RideMessage rideMessage) {

        rabbitTemplate.convertAndSend("rideQueue", rideMessage);
    }


}
