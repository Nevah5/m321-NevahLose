package dev.geeler.apiaces.playerservice.model;

import jakarta.persistence.*; // TODO: Remove star import
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

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(
            name = "player_authorities",
            joinColumns = @JoinColumn(name = "player_id")
    )
    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
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
        return UserDetails.super.isEnabled(); // TODO: make accounts expire
    }

    public static class Builder {
        private final Player player;

        public Builder() {
            player = new Player();
            player.id = UUID.randomUUID();
            player.authorities = Set.of(Role.USER);
        }

        public Builder setUsername(String username) {
            player.username = username;
            return this;
        }

        public Builder addAuthority(Role authority) {
            player.authorities.add(authority);
            return this;
        }

        public Player build() {
            return player;
        }
    }
}
