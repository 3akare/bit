package com.bit.app.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class ClientIdentifyResolver {
    private static final String HEADER_DEVICE_ID = "X-Device-ID";
    public static final String COOKIE_NAME = "d_id";

    public String resolveIdentity(HttpServletRequest request){
        String headerId = request.getHeader(HEADER_DEVICE_ID);
        if (headerId != null && !headerId.isBlank()){
            return "device" + headerId;
        }

        if (request.getCookies() != null){
            Optional<String> cookieID = Arrays.stream(request.getCookies())
                    .filter(c -> COOKIE_NAME.equals(c.getName()))
                    .map(Cookie::getValue)
                    .findFirst();
            if (cookieID.isPresent()) return "cookie:" + cookieID.get();
        }

        return "ip:" + getClientIp(request);
    }

    private String getClientIp(HttpServletRequest request){
        String xForwardFor = request.getHeader("x-Forwarded-For");
        if (xForwardFor != null && !xForwardFor.isBlank()){
            return xForwardFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
