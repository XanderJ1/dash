package com.bash.dash.messaging;

import com.bash.dash.messaging.dtos.RideMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RideConsumer {

    private final SimpMessagingTemplate messagingTemplate;

    public RideConsumer(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "rideQueue")
    public void receiveMessage(RideMessage rideMessage) {
        messagingTemplate.convertAndSend(
                "/topic/riders/" + rideMessage.riderId() + "/rides",
                rideMessage
        );
    }

}
