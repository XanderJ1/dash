package com.bash.dash.config;

import com.bash.dash.location.models.DriverLocation;
import com.bash.dash.location.models.GeoPoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
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
                        driverId,
                        new GeoPoint(latitude,longitude)
                );

        log.info(
                "Sending driver location to WebSocket: destination={}, message={}",
                destination,
                message
        );
        messagingTemplate.convertAndSend(
                destination,
                message
        );
    }
}