package com.bash.dash.messaging.dtos;

public record LocationMessage(
        Double lat,
        Double lng,
        Long id
) {
}
