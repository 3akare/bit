package com.bit.app.service.impl;

import com.bit.app.service.RateLimitService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class RateLimitServiceImpl implements RateLimitService {
    private final ProxyManager<String> proxyManager;

    @Override
    public boolean tryConsume(String key) {
        Supplier<BucketConfiguration> configurationSupplier = getConfigSupplier();
        return proxyManager.builder().build(key, configurationSupplier).tryConsume(1);
    }

    @Override
    public Supplier<BucketConfiguration> getConfigSupplier() {
        return () -> BucketConfiguration.builder()
                .addLimit(Bandwidth.builder()
                        .capacity(10)
                        .refillIntervally(10, Duration.ofHours(1))
                        .build())
                .build();
    }
}
