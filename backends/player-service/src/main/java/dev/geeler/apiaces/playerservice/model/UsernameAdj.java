package dev.geeler.apiaces.playerservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity(name = "username_adj")
@Table(name = "x_username_adj")
public class UsernameAdj {
    @Id
    @Getter
    private Long id;

    @Getter
    private String value;
}
