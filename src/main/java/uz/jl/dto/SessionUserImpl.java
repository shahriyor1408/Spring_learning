package uz.jl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.jl.configs.security.SessionUser;
import uz.jl.domains.AuthUser;

import java.time.LocalDateTime;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionUserImpl implements SessionUser {
    private AuthUser user;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private String sessionSecret;


    @Override
    public String getPrinciple() {
        return user.getUsername();
    }

    @Override
    public String getCredentials() {
        return user.getPassword();
    }

    @Override
    public Boolean isActive() {
        return true;
    }

    @Override
    public LocalDateTime issuedAt() {
        return issuedAt;
    }

    @Override
    public LocalDateTime expiresAt() {
        return expiresAt;
    }

    @Override
    public Boolean isExpired() {
        return false;
    }

    @Override
    public String sessionSecret() {
        return sessionSecret;
    }
}
