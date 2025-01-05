package dev.geeler.apiaces.gameservice.model.http;

import org.springframework.http.HttpStatus;

import java.util.UUID;

public class ServerErrorResponse extends HttpResponse {
    public final UUID errorCode;

    public ServerErrorResponse(HttpStatus code, String message) {
        super(code, message);
        this.errorCode = UUID.randomUUID();
    }

    @Override
    public String toString() {
        return "ServerErrorResponse{" +
                "errorCode=" + errorCode +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}