package uz.jl.filters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.jl.configs.security.SecurityContextHolder;
import uz.jl.configs.security.SessionUser;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    private final SecurityContextHolder contextHolder;
    private final Predicate<Cookie> checkSessionIdCookiePredicate = cookie -> cookie.getName().equals("JSESSIONID");

    private final List<String> WHITE_LIST = List.of("/auth/login", "/auth/register");
    private final Predicate<String> isSecure = path -> !WHITE_LIST.contains(path);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (!isSecure.test(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            System.out.println("unauthorized");
            response.sendRedirect("/auth/login");
            filterChain.doFilter(request, response);
            return;
        }

        Optional<Cookie> sessionCookie = Arrays.stream(cookies).filter(checkSessionIdCookiePredicate).findFirst();
        if (sessionCookie.isEmpty()) {
            System.out.println("unauthorized");
            response.sendRedirect("/auth/login");
            filterChain.doFilter(request, response);
            return;
        }

        Cookie cookie = sessionCookie.get();
        String value = cookie.getValue();

        SessionUser sessionUser = contextHolder.context.get(value);
        if (Objects.isNull(sessionUser)) {
            System.out.println("sessionid is invalid");
            response.sendRedirect("/auth/login");
            return;
        }

        // TODO: 8/1/2022 secret check here
        if (LocalDateTime.now(Clock.systemDefaultZone()).isAfter(sessionUser.expiresAt())) {
            System.out.println("Session expired");
            response.sendRedirect("/auth/login");
            return;
        }

        if (!sessionUser.isActive()) {
            System.out.println("user not active");
            response.sendRedirect("/auth/login");
            return;
        }


        filterChain.doFilter(request, response);
    }
}
