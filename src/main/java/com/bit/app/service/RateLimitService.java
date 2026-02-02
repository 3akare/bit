package com.bit.app.service;

import io.github.bucket4j.BucketConfiguration;

import java.util.function.Supplier;

public interface RateLimitService {
    boolean tryConsume(String key);
    Supplier<BucketConfiguration> getConfigSupplier();
}
