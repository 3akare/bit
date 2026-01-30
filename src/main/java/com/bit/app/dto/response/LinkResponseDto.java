package com.bit.app.dto.response;

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
    private String linkUrl;
    private String alias;
    private String shortCode;
    private Instant expiresAt;
}
