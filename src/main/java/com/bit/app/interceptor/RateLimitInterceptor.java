package com.bit.app.interceptor;

import com.bit.app.service.RateLimitService;
import com.bit.app.util.ClientIdentifyResolver;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {
    private final RateLimitService rateLimitService;
    private final ClientIdentifyResolver identifyResolver;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler){
        String apiKey = identifyResolver.resolveIdentity(request);
        if (apiKey.startsWith("ip:")){
            String newDeviceId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(ClientIdentifyResolver.COOKIE_NAME, newDeviceId);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 365);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }

        if (!rateLimitService.tryConsume(apiKey)){
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            try {
                response.getWriter().write("{\"error\": \"Too many requests. Limit: 10/hour\"}");
                response.setContentType("application/json");
            } catch (Exception e) {
                log.info("Error occurred with rate limit service writer");
            }
            return false;
        }
        return true;
    }
}
