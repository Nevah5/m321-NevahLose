package dev.geeler.apiaces.playerservice.model;

import org.springframework.http.HttpStatus;

public class ErrorResponse extends HttpResponse {
    public ErrorResponse(HttpStatus code, String message) {
        super(code, message);
    }
}
