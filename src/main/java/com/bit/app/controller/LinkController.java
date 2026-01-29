package com.bit.app.controller;

import com.bit.app.dto.ShortenRequest;
import com.bit.app.service.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1")
public class LinkController {
    private final LinkService linkService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectURL(@PathVariable String shortCode) {
        String originalUrl = linkService.getOriginalUrl(shortCode);
        if (originalUrl.isBlank())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        String absoluteUrl = originalUrl.contains("://") ? originalUrl : "https://" + originalUrl;
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(absoluteUrl))
                .build();
    }

    @PostMapping("/shorten")
    public ResponseEntity<String> shorten(@RequestBody @Valid ShortenRequest request) {
        String shortCode = linkService.shorten(request.getUrl());
        return ResponseEntity.ok(shortCode);
    }
}
