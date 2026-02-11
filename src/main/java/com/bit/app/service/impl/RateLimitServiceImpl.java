package com.bit.app.service.impl;

import com.bit.app.service.RateLimitService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateLimitServiceImpl implements RateLimitService {

    private final ProxyManager<String> proxyManager;
    private final ConcurrentMap<String, Bucket> localBuckets =
        new ConcurrentHashMap<>();

    public RateLimitServiceImpl(
        @Autowired(required = false) ProxyManager<String> proxyManager
    ) {
        this.proxyManager = proxyManager;
    }

    @Override
    public boolean tryConsume(String key) {
        if (proxyManager != null) {
            Supplier<BucketConfiguration> configurationSupplier =
                getConfigSupplier();
            return proxyManager
                .builder()
                .build(key, configurationSupplier)
                .tryConsume(1);
        }

        Bucket bucket = localBuckets.computeIfAbsent(key, ignored ->
            Bucket.builder()
                .addLimit(
                    Bandwidth.builder()
                        .capacity(10)
                        .refillIntervally(10, Duration.ofHours(1))
                        .build()
                )
                .build()
        );
        return bucket.tryConsume(1);
    }

    @Override
    public Supplier<BucketConfiguration> getConfigSupplier() {
        return () ->
            BucketConfiguration.builder()
                .addLimit(
                    Bandwidth.builder()
                        .capacity(10)
                        .refillIntervally(10, Duration.ofHours(1))
                        .build()
                )
                .build();
    }
}
