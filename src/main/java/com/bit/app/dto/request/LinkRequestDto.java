package com.bit.app.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.Instant;

@Data
@JsonPropertyOrder({ "link_url", "alias", "expires_at" })
public class LinkRequestDto {
    @JsonProperty("link_url")
    @NotBlank(message = "link_url is required")
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$", message = "Please enter a valid URL or domain")
    @Schema(description = "The original long URL to be shortened", example = "https://www.google.com")
    private String linkUrl;

    @Size(max = 20, min = 1, message = "alias must be 20 characters or less")
    @Schema(description = "Optional custom alias for the link", example = "my-custom-link")
    private String alias;

    @Future(message = "expires_at must be in the future")
    @JsonProperty(value = "expires_at")
    @Schema(description = "Expiration timestamp for the link. If null, link never expires.", example = "2026-12-31T23:59:59Z")
    private Instant expiresAt;
}