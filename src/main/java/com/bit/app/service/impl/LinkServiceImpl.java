package com.bit.app.service.impl;

import com.bit.app.dto.response.DefaultApiResponse;
import com.bit.app.dto.response.LinkResponseDto;
import com.bit.app.entity.Link;
import com.bit.app.repository.LinkRepository;
import com.bit.app.service.LinkService;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;
    private final Optional<StringRedisTemplate> redisTemplate;
    private final com.bit.app.service.AnalyticsService analyticsService;
    private static final String CACHE_PREFIX = "links:";

    @Override
    public String getUrl(String code) {
        String cacheKey = CACHE_PREFIX + code;

        String cachedUrl = getCachedUrl(cacheKey);
        if (cachedUrl != null) return cachedUrl;

        return linkRepository
            .findValidLink(code, Instant.now())
            .map(link -> {
                if (link.getExpiresAt() != null) {
                    long secondsToLive = Duration.between(
                        Instant.now(),
                        link.getExpiresAt()
                    ).getSeconds();
                    if (secondsToLive > 0) {
                        cacheUrl(
                            cacheKey,
                            link.getUrl(),
                            Duration.ofSeconds(secondsToLive)
                        );
                    }
                } else {
                    cacheUrl(cacheKey, link.getUrl(), Duration.ofDays(1));
                }
                return link.getUrl();
            })
            .orElse(null);
    }

    private String getCachedUrl(String cacheKey) {
        if (redisTemplate.isEmpty()) {
            return null;
        }
        try {
            return redisTemplate.get().opsForValue().get(cacheKey);
        } catch (Exception exception) {
            log.debug(
                "Skipping Redis cache read for key {}",
                cacheKey,
                exception
            );
            return null;
        }
    }

    private void cacheUrl(String cacheKey, String url, Duration duration) {
        if (redisTemplate.isEmpty()) {
            return;
        }
        try {
            redisTemplate.get().opsForValue().set(cacheKey, url, duration);
        } catch (Exception exception) {
            log.debug(
                "Skipping Redis cache write for key {}",
                cacheKey,
                exception
            );
        }
    }

    @Override
    @Async
    public void incrementClick(String code) {
        linkRepository.incrementClickCount(code);
        linkRepository
            .findByShortCodeOrAlias(code, code)
            .ifPresent(link ->
                analyticsService.notifyClick(
                    link.getShortCode(),
                    link.getAlias(),
                    link.getClickCount()
                )
            );
    }

    @Override
    @Transactional
    public DefaultApiResponse<LinkResponseDto> encode(
        String url,
        String alias,
        Instant expiresAt
    ) {
        String finalShortCode;
        int retryCount = 0;

        if (alias != null && !alias.isBlank()) {
            if (
                linkRepository.existsByAlias(alias) ||
                linkRepository.existsByShortCode(alias)
            ) {
                alias =
                    alias + "-" + UUID.randomUUID().toString().substring(0, 4);
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

        return DefaultApiResponse.<LinkResponseDto>builder()
            .statusCode(HttpStatus.CREATED.value())
            .stateMessage("Success")
            .data(new LinkResponseDto(url, alias, finalShortCode, expiresAt))
            .build();
    }

    private String generateUniqueCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
