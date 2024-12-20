package dev.geeler.apiaces.playerservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class JwtResponseDto {
    @Getter
    private final String token;

    @Getter
    @JsonProperty("expires_in")
    private final long expiresIn;

    @Getter
    private String type = "Bearer";

    public JwtResponseDto(final String token, final long expiresIn, final String type) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.type = type;
    }

    public JwtResponseDto(final String token, final long expiresIn) {
        this.token = token;
        this.expiresIn = expiresIn;
    }
}
