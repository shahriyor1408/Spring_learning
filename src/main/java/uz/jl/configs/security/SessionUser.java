package uz.jl.configs.security;

import java.time.LocalDateTime;

public interface SessionUser {
    String getPrinciple();

    String getCredentials();

    Boolean isActive();

    LocalDateTime issuedAt();

    LocalDateTime expiresAt();

    Boolean isExpired();

    String sessionSecret();
}
