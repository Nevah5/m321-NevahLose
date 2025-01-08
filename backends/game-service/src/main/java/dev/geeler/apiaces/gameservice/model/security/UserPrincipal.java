package dev.geeler.apiaces.gameservice.model.security;

import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserPrincipal {
    private UUID id;
    private String username;
}
