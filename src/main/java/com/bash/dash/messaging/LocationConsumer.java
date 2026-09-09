package com.bash.dash.messaging;

import com.bash.dash.messaging.dtos.LocationMessage;
import com.bash.dash.location.service.LocationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class LocationConsumer {

    private final LocationService  locationService;


    public LocationConsumer(LocationService locationService) {
        this.locationService = locationService;
    }

    @RabbitListener(queues = "locationQueue")
    public void receiveMessage(LocationMessage locationMessage) {
        locationService.updateLocation(locationMessage.lat(), locationMessage.lng(), locationMessage.id());
    }



}