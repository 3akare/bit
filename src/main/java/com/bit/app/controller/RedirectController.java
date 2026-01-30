package com.bit.app.controller;

import com.bit.app.service.LinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class RedirectController {
    private final LinkService linkService;

    @Value("${app.base-url:https://3akare.vercel.app}")
    private String fallbackUrl;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        String url = linkService.getUrl(shortCode);

        if (url != null) {
            linkService.incrementClick(shortCode);
            if (!url.startsWith("http")) url = "https://" + url;
            return ResponseEntity.status(HttpStatus.FOUND)
                    .location(URI.create(url))
                    .build();
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(fallbackUrl))
                .build();
    }
}