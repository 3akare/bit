package com.bit.app.controller;

import com.bit.app.dto.request.LinkShortenRequestDto;
import com.bit.app.dto.response.ApiResponse;
import com.bit.app.dto.response.LinkResponseDto;
import com.bit.app.service.LinkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class LinkController {
    private final LinkService linkService;

    @PostMapping("/shorten")
    public ResponseEntity<ApiResponse<LinkResponseDto>> encode(@RequestBody @Valid LinkShortenRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(linkService.encode(
                        request.getLinkUrl(),
                        request.getAlias(),
                        request.getExpiresAt())
                );
    }
}
