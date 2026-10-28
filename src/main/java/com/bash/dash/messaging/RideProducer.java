package com.bash.dash.messaging;

import com.bash.dash.messaging.dtos.RideMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RideProducer {

    private final RabbitTemplate rabbitTemplate;

    public RideProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(RideMessage rideMessage) {

        log.info("Sending ride to queue");
        rabbitTemplate.convertAndSend("rideQueue", rideMessage);
    }


}
