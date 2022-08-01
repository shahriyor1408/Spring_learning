package uz.jl.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uz.jl.domains.AuthUser;
import uz.jl.dto.LoginDTO;
import uz.jl.dto.SessionUserImpl;
import uz.jl.repository.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SecurityContextHolder {
    @Autowired
    private UserRepository userRepository;


    @Autowired
    private PasswordEncoder passwordEncoder;


    public final static ConcurrentHashMap<String, SessionUser> context = new ConcurrentHashMap<>();

    public void authenticate(LoginDTO map, HttpServletResponse response) {
        String username = map.getUsername();
        String password = map.getPassword();
        AuthUser authUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.match(password, authUser.getPassword())) {
            throw new RuntimeException("Bad credentials");
        }

        String secret = passwordEncoder.encode(authUser.getUsername());

        SessionUser sessionUser = SessionUserImpl.builder()
                .user(authUser)
                .issuedAt(LocalDateTime.now(Clock.systemDefaultZone()))
                .expiresAt(LocalDateTime.now(Clock.systemDefaultZone()).plusMinutes(1))
                .sessionSecret(secret)
                .build();
        context.put(secret, sessionUser);
        Cookie cookie = new Cookie("JSESSIONID", secret);
        cookie.setMaxAge(60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
