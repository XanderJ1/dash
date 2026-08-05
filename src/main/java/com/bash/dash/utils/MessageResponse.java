package com.bash.dash.utils;

public record MessageResponse(String message) {
}
enum CODE {
    NOT_FOUND, FORBIDDEN, UNAUTHORIZED
}