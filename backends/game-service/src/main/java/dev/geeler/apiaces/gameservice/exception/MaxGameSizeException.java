package dev.geeler.apiaces.gameservice.exception;

public class MaxGameSizeException extends RuntimeException {
    public MaxGameSizeException(String message) {
        super(message);
    }
}
