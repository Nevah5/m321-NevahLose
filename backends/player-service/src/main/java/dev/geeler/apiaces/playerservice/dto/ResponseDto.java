package dev.geeler.apiaces.playerservice.dto;

public class ResponseDto {
    private String message;

    public ResponseDto() {
    }

    public ResponseDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
