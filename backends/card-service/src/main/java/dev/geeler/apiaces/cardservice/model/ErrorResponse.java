package dev.geeler.apiaces.cardservice.model;

import org.springframework.http.HttpStatus;

public class ErrorResponse extends HttpResponse {
    public ErrorResponse(HttpStatus code, String message) {
        super(code, message);
    }

    public String toJsonString() {
        return "{\"code\": "+ this.code + ", \"message\": \"" + this.message + "\"}";
    }
}
