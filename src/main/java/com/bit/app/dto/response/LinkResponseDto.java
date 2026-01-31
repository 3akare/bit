package com.bit.app.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkResponseDto {
    @Schema(description = "The original long URL", example = "https://www.google.com")
    private String linkUrl;
    @Schema(description = "Custom alias if provided", example = "my-custom-link")
    private String alias;
    @Schema(description = "The generated or provided short code", example = "a1b2c3d4")
    private String shortCode;
    @Schema(description = "Expiration timestamp", example = "2026-12-31T23:59:59Z")
    private Instant expiresAt;
}
