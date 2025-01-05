package dev.geeler.apiaces.cardservice.model;

import org.springframework.http.HttpStatus;

public abstract class HttpResponse {
    public final int code;
    public final String message;

    public HttpResponse(HttpStatus code, String message) {
        this.code = code.value();
        this.message = message;
    }
}
