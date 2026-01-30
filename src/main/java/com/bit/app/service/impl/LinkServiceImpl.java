package com.bit.app.service.impl;

import com.bit.app.dto.response.ApiResponse;
import com.bit.app.dto.response.LinkResponseDto;
import com.bit.app.entity.Link;
import com.bit.app.repository.LinkRepository;
import com.bit.app.service.LinkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkServiceImpl implements LinkService {
    private final LinkRepository linkRepository;

    @Override
    @Cacheable(value = "links", key = "#code", unless = "#result == null")
    public String getUrl(String code) {
        return linkRepository.findValidLink(code, Instant.now())
                .map(Link::getUrl)
                .orElse(null);
    }

    @Override
    @Async
    public void incrementClick(String code) {
        linkRepository.incrementClickCount(code);
    }

    @Override
    @Transactional
    public ApiResponse<LinkResponseDto> encode(String url, String alias, Instant expiresAt) {
        String finalShortCode;
        if (alias != null && !alias.isBlank()) {
            if (linkRepository.existsByAlias(alias) || linkRepository.existsByShortCode(alias)) {
                throw new IllegalArgumentException("Alias already in use");
            }
            finalShortCode = alias;
        } else {
            finalShortCode = generateUniqueCode();
        }

        Link link = new Link();
        link.setUrl(url);
        link.setAlias(alias);
        link.setShortCode(finalShortCode);
        link.setExpiresAt(expiresAt);
        link.setClickCount(0L);

        linkRepository.save(link);

        return ApiResponse.<LinkResponseDto>builder()
                .statusCode(HttpStatus.CREATED.value())
                .stateMessage("Success")
                .data(new LinkResponseDto(url, alias, finalShortCode, expiresAt))
                .build();
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}