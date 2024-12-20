package dev.geeler.apiaces.playerservice.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.UUID;

@Entity(name = "player")
@Table(name = "players")
public class Player implements UserDetails {
    @Id
    @Getter
    private UUID id;

    @Getter
    private String username;

    @ElementCollection(targetClass = Role.class)
    @Getter
    private Set<Role> authorities;

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public static class Builder {
        private final Player player;

        public Builder() {
            player = new Player();
            player.id = UUID.randomUUID();
        }

        public Builder setUsername(String username) {
            player.username = username;
            return this;
        }

        public Player build() {
            return player;
        }
    }
}
