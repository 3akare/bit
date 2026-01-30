package com.bit.app.service;

import com.bit.app.dto.response.ApiResponse;
import com.bit.app.dto.response.LinkResponseDto;

import java.time.Instant;

public interface LinkService {
    String getUrl(String shortCode);
    void incrementClick(String code);
    ApiResponse<LinkResponseDto> encode(String url, String alias, Instant expiresAt);
}
