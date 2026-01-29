package com.bit.app.controller;

import com.bit.app.dto.ShortenRequest;
import com.bit.app.service.impl.ILinkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class LinkController {
    private final ILinkService iLinkService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirectURL(@PathVariable String shortCode){
        String URL = iLinkService.redirectURL(shortCode);
        if (URL.isBlank())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        String absoluteUrl = URL.contains("://") ? URL : "https://" + URL;
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(absoluteUrl))
                .build();
    }

    @PostMapping
    public ResponseEntity<Void> shorten(@RequestBody ShortenRequest request){
        iLinkService.shorten(request.getUrl());
        return ResponseEntity.ok().build();
    }
}
