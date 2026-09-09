package com.bash.dash.config;

import com.bash.dash.location.models.DriverLocation;
import com.bash.dash.location.models.GeoPoint;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class DriverLocationWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public DriverLocationWebSocketService(
            SimpMessagingTemplate messagingTemplate
    ) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendDriverLocation(
            Long driverId,
            Double latitude,
            Double longitude
    ) {

        String destination =
                "/topic/drivers/" + driverId + "/location";

        DriverLocation message =
                new DriverLocation(
                        1L,
                        new GeoPoint(latitude,longitude)
                );

        messagingTemplate.convertAndSend(
                destination,
                message
        );
    }
}