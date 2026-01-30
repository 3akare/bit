package com.bit.app.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.time.Instant;

@Data
@JsonPropertyOrder({ "link_url", "alias", "expires_at" })
public class LinkShortenRequestDto {
    @JsonProperty("link_url")
    @NotBlank(message = "link_url is required")
    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$",  message = "Please enter a valid URL or domain")
    private String linkUrl;

    @Size(max = 20, min = 1, message = "alias must be 20 characters or less")
    private String alias;

    @Future(message = "expires_at must be in the future")
    @JsonProperty(value = "expires_at")
    private Instant expiresAt;
}