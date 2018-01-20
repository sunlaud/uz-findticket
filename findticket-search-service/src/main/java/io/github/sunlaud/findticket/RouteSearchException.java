package io.github.sunlaud.findticket;

public class RouteSearchException extends RuntimeException {
    public RouteSearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouteSearchException(String message) {
        super(message);
    }
}
