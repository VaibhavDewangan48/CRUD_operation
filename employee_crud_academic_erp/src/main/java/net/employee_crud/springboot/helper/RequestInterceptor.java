package net.employee_crud.springboot.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RequestInterceptor implements HandlerInterceptor {
    private final JWTHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Skip token validation for login and register routes
        String path = request.getRequestURI();

        // If the request is for login or register, don't check token
        if (path.contains("/login") || path.contains("/register")) {
            return true;
        }

        // Otherwise, check for the Authorization header
        String authorizationHeader = request.getHeader("Authorization");

        // If there's no Authorization header or it's not a Bearer token, return unauthorized
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // Extract the token from the Authorization header
        String token = authorizationHeader.substring(7);
        String email = jwtHelper.extractEmail(token);

        // If the token is invalid or the email can't be extracted, return unauthorized
        if (email == null || !jwtHelper.validateToken(token, email)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // If everything is valid, allow the request to proceed
        return true;
    }
}
